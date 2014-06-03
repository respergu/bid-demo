package demo;

import highcharts.Axis;
import highcharts.Chart;
import highcharts.Credits;
import highcharts.HighchartsNgConfig;
import highcharts.Options;
import highcharts.PlotOptions;
import highcharts.Series;
import highcharts.SeriesOptions;
import highcharts.Title;
import highcharts.Tooltip;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import power.PowerSearchNode;
import power.PowerSearchQuery;
import power.SchemaProperty;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.Tuple;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.StringPath;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

@Component
class BookingCommandLineRunner implements CommandLineRunner {
	@Autowired BookingRepository bookingRepository; 
	@Autowired KeywordCostsDao keywordCostsDao;
	@Autowired EntityManager em;
	
	@Override
	public void run(String... args) throws Exception {
		for(Booking booking : this.bookingRepository.findAll()) {
			System.out.println(booking.getBookingName());
		}
		
		JPAQuery query = new JPAQuery(em);
		QBooking booking = QBooking.booking;
		List<Booking> booList = query.from(booking).where(booking.bookingName.eq("Josh")
				.or(booking.bookingName.eq("Kris"))).list(booking);
		for (Booking booking2 : booList) {
			System.out.println("Using QBooking : " + booking2.getBookingName());
		}
		
		QKeywordCosts kwCosts = QKeywordCosts.keywordCosts;
		
		Class<?> entityClass = Class.forName("demo.KeywordCosts");
		// "entity" is the variable name of the path
		PathBuilder<?> entityPath = new PathBuilder(entityClass, "keywordCosts");
		
		Date fromDate =  DateTime.now().minusDays(8).withHourOfDay(0).withHourOfDay(0).withMinuteOfHour(0).toDate();
		Date toDate = DateTime.now().withHourOfDay(0).withHourOfDay(0).withMinuteOfHour(0).toDate();
//		List<KeywordCosts> kwList = kwQuery.from(kwCosts)
//				.where(kwCosts.key.loadDate.after(fromDate)
//				.and(kwCosts.key.loadDate.before(toDate))).list(kwCosts);
//		
//		for (KeywordCosts keywordCosts : kwList) {
//			System.out.println("keywordCosts : " +  keywordCosts.getKey().getLoadDate() + "  -  " + keywordCosts);
//		}
//		
//		Long InquSum = kwQuery.uniqueResult(kwCosts.inquiries.sum());
//		System.out.println("sum of inquiries =  " + InquSum);
//		
//		Integer clickSum = kwQuery.uniqueResult(kwCosts.clicks.sum());
//		System.out.println("sum of clicks =  " + clickSum);
//		
//		List<Long> sumInqQueryList = kwQuery.groupBy(kwCosts.key.uniqueId).having(kwCosts.inquiries.sum().eq(571L)).list(kwCosts.key.uniqueId);
//		System.out.println("sumInqQueryList : " + sumInqQueryList);
//		
//		kwQuery.where(kwCosts.key.uniqueId.in(new JPASubQuery().from(kwCosts).groupBy(kwCosts.key.uniqueId).having(kwCosts.inquiries.sum().eq(571L)).list(kwCosts.key.uniqueId) ));
//		
//		List<KeywordCosts> kwInqList = kwQuery.list(kwCosts);
//		
//		for (KeywordCosts keywordCosts : kwInqList) {
//			System.out.println("keywordCosts : " + keywordCosts);
//		}
		
		JPAQuery kwordQuery = new JPAQuery(em);
		QKeyword qKeyword = QKeyword.keyword1;
		QListId qListId = QListId.listId;
		
		JPAQuery jpaQuery = kwordQuery.from(qKeyword);
		jpaQuery.distinct();
		jpaQuery.join(qKeyword.lists, qListId);
		jpaQuery.leftJoin(qKeyword.costs, kwCosts);
		
		Predicate p0 = qKeyword.keyword.contains("grants");
		Predicate p1 = qKeyword.keywordType.contains("Phrase");
		Predicate p2 = qListId.listName.contains("test");
		Predicate p3 = kwCosts.key.loadDate.after(fromDate);
		Predicate p4 = kwCosts.key.loadDate.before(toDate);
		
		NumberPath<Long> numberPath = entityPath.getNumber("impressions", Long.class);
		NumberPath<BigDecimal> bigDecimalPath = entityPath.getNumber("costDollars", BigDecimal.class);
		
		Predicate p5 = qKeyword.id.in(new JPASubQuery().from(kwCosts).where(p3).where(p4).groupBy(kwCosts.key.uniqueId).having(bigDecimalPath.sum().gt(10L)).list(kwCosts.key.uniqueId) );
		
		BooleanBuilder builder = new BooleanBuilder();
		
		builder.and(p0);
		builder.and(p1);
		builder.and(p2);
		builder.and(p5);

		JPAQuery where = jpaQuery.where(builder);
		List<Tuple> kList = where.list(qKeyword.account, qKeyword.engineId);
		for (Tuple keyword : kList) {
			System.out.println("qKeyword.account : " + qKeyword.account + " " + keyword.get(qKeyword.account));
			System.out.println("qKeyword.engineId : " + qKeyword.engineId + " " + keyword.get(qKeyword.engineId));
		}
		
	}
}

interface BookingRepository extends JpaRepository<Booking, Long> {
	Collection<Booking> findByBookingName(String bookingName);
}

interface KeywordCostsDao extends JpaRepository<KeywordCosts, ComponentCostsKey> {
	KeywordCosts findByKeyLoadDate(Date loadDate);
	Collection<KeywordCosts> findByKeyLoadDateBetween(Date fromDate, Date toDate);
	Collection<KeywordCosts> findByKeyLoadDateGreaterThan(Date loadDate);
}

@RestController
class BookingRestController {
	@Autowired BookingRepository bookingRepository;
	@Autowired KeywordCostsDao keywordCostsDao;
	@Autowired EntityManager em;
	
	@RequestMapping("/bookings")
	Collection<Booking> bookings (){
		return this.bookingRepository.findAll();
	}
	
	@RequestMapping("/keywordCosts")
	HighchartsNgConfig keywordCostsChart (@RequestBody ChartRequest request){
		Date r1DateFrom = request.getRange1FromDate();
		Date r1DateTo = request.getRange1ToDate();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yy");
		Collection<KeywordCosts> loadDateBetween = keywordCostsDao.findByKeyLoadDateBetween(r1DateFrom, r1DateTo);
		
		Chart chart = new Chart(1, null);
		Credits credits = new Credits(false);
		SeriesOptions seriesOptions = new SeriesOptions(true);
		PlotOptions plotOptions = new PlotOptions(null , seriesOptions);
		Tooltip tooltip = new Tooltip("", "", "", false, true);
		Axis xAxis = new Axis();
		String titleName = getTitleName(request);
		Title xtitle = new Title(titleName);
		xAxis.setTitle(xtitle);
		Collection<String> categories = new ArrayList<String>();
		for (KeywordCosts kwCosts : loadDateBetween) {
			categories.add(simpleDateFormat.format(kwCosts.getKey().getLoadDate()));
		}
		xAxis.setEndOnTick(false);
		xAxis.setTickInterval((int)categories.size()/14);
		xAxis.setCategories(categories);
		Axis yAxis = new Axis();
		Title title = new Title("Cost Metrics");
		Options options = new Options(chart  , credits , plotOptions, title  , tooltip , xAxis , yAxis);
		Collection<Series> series = new ArrayList<Series>();
		
		addRevenueSerie(loadDateBetween, series, "solid", 0, "Revenue", true, true);
		addNCSerie(loadDateBetween, series, "solid", 0, "Net Contribution", true, true);
		addCostDollarsSerie(loadDateBetween, series, "solid", 0, "Cost", true, true);
		addClicksSerie(loadDateBetween, series, "solid", 0, "Clicks", true, false);
		
		if (request.isCompareDates()) {
			Date r2DateFrom = request.getRange2FromDate();
			Date r2DateTo = request.getRange2ToDate();
			Collection<KeywordCosts> secondRangeCosts = keywordCostsDao.findByKeyLoadDateBetween(r2DateFrom, r2DateTo);
			
			Axis xAxis2 = new Axis();
			titleName = "Custom " + simpleDateFormat.format(request.getRange2FromDate()) + " to "+ simpleDateFormat.format(request.getRange2ToDate());
			Title xtitle2 = new Title(titleName);
			xAxis2.setOpposite(true);
			xAxis2.setTitle(xtitle2);
			Collection<String> categories2 = new ArrayList<String>();
			for (KeywordCosts kwCosts : secondRangeCosts) {
				categories2.add(simpleDateFormat.format(kwCosts.getKey().getLoadDate()));
			}
			xAxis2.setEndOnTick(false);
			xAxis2.setTickInterval((int)categories2.size()/14);
			xAxis2.setCategories(categories2);

			addRevenueSerie(secondRangeCosts, series, "ShortDot", 1, "Revenue", false, true);
			addNCSerie(secondRangeCosts, series, "ShortDot", 1, "Net Contribution", false, true);
			addCostDollarsSerie(secondRangeCosts, series, "ShortDot", 1, "Cost", false, true);
			addClicksSerie(secondRangeCosts, series, "ShortDot", 1, "Clicks", false, false);
			options.setxAxis2(xAxis2);
		}
		
		HighchartsNgConfig chartCongif = new HighchartsNgConfig(options, series);
		return chartCongif;
	}

	private String getTitleName(ChartRequest request) {
		String titleName = "";
		if (request.getDateRange().intValue() == 0) {
			titleName = "Last 7 Days";
		} else if (request.getDateRange().intValue() == 1) {
			titleName = "Today";
		} else if (request.getDateRange().intValue() == 2) {
			titleName = "Yesterday";
		} else if (request.getDateRange().intValue() == 3) {
			titleName = "Last Week";
		} else if (request.getDateRange().intValue() == 4) {
			titleName = "Current Month";
		} else if (request.getDateRange().intValue() == 5) {
			titleName = "Last Month";
		} else if (request.getDateRange().intValue() == 6) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yy");
			titleName = "Custom " + simpleDateFormat.format(request.getRange1FromDate()) + " to "+ simpleDateFormat.format(request.getRange1ToDate());
		}
		return titleName;
	}

	private void addRevenueSerie(Collection<KeywordCosts> loadDateBetween, Collection<Series> series, String dashStyle, int xAxis, String name, boolean showInLegend, boolean visible) {
		String color = "#4ac925";
		Collection<Number> data = new ArrayList<Number>();
		for (KeywordCosts kwCosts : loadDateBetween) {
			data.add(kwCosts.getRevenue());
		}
		String type = "line";
		Series serie = new Series(color , data, name, type, showInLegend, visible);
		serie.setDashStyle(dashStyle);
		serie.setyAxis(0);
		serie.setxAxis(xAxis);
		series.add(serie);
	}
	
	private void addNCSerie(Collection<KeywordCosts> loadDateBetween, Collection<Series> series, String dashStyle, int xAxis, String name, boolean showInLegend, boolean visible) {
		String color = "#0715cd";
		Collection<Number> data = new ArrayList<Number>();
		for (KeywordCosts kwCosts : loadDateBetween) {
			data.add(kwCosts.getNetContribution());
		}
		String type = "line";
		Series serie = new Series(color , data, name, type, showInLegend, visible);
		serie.setDashStyle(dashStyle);
		serie.setyAxis(0);
		serie.setxAxis(xAxis);
		series.add(serie);
	}
	
	private void addCostDollarsSerie(Collection<KeywordCosts> loadDateBetween, Collection<Series> series, String dashStyle, int xAxis, String name, boolean showInLegend, boolean visible) {
		String color = "#e00707";
		Collection<Number> data = new ArrayList<Number>();
		for (KeywordCosts kwCosts : loadDateBetween) {
			data.add(kwCosts.getCostDollars());
		}
		String type = "line";
		Series serie = new Series(color , data, name, type, showInLegend, visible);
		serie.setDashStyle(dashStyle);
		serie.setyAxis(0);
		serie.setxAxis(xAxis);
		series.add(serie);
	}
	
	private void addClicksSerie(Collection<KeywordCosts> loadDateBetween, Collection<Series> series, String dashStyle, int xAxis, String name, boolean showInLegend, boolean visible) {
		String color = "#626262";
		Collection<Number> data = new ArrayList<Number>();
		for (KeywordCosts kwCosts : loadDateBetween) {
			data.add(kwCosts.getClicks());
		}
		String type = "line";
		Series serie = new Series(color , data, name, type, showInLegend, visible);
		serie.setDashStyle(dashStyle);
		serie.setyAxis(1);
		serie.setxAxis(xAxis);
		series.add(serie);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/powerSearch", method = RequestMethod.POST)
	List<Keyword> powerSearch (@RequestBody PowerSearchQuery query) throws Exception {
		List<SchemaProperty> schemaInfo = query.getSchemaInfo();
		// entityClass is the entity type, not the Q-type
		Class<?> entityClass = Keyword.class;
		// "entity" is the variable name of the path
		PathBuilder<?> entityPath = new PathBuilder(entityClass, "keyword1");
		
		JPAQuery kwordQuery = new JPAQuery(em);
		QKeyword qKeyword = QKeyword.keyword1;
		JPAQuery jpaQuery = kwordQuery.from(qKeyword).distinct();
		
		BooleanBuilder builder = new BooleanBuilder();
		for (PowerSearchNode powerSearchNode : query.getNodes()) {
			 if (powerSearchNode.getOp().getType().equals("group")) {
				 System.out.println("process node group : " + powerSearchNode.getNodes());
				 if (powerSearchNode.getOp().getDescription().equals("or")) {
					 List<Predicate> orNodes = getOrGroupPredicates(schemaInfo,entityPath, qKeyword, jpaQuery, powerSearchNode);
					 builder.andAnyOf(orNodes.toArray(new Predicate[orNodes.size()]));
				 }
			 } else {
				 System.out.println("process node : " + powerSearchNode);
				 processPowerSearchNode(schemaInfo, entityPath, qKeyword, jpaQuery, builder, powerSearchNode);
			 }
		}
		
		if (query.getMetricNodes().size() > 0) {
			QKeywordCosts kwCosts = QKeywordCosts.keywordCosts;
			Class<?> entityCostClass = Class.forName("demo.KeywordCosts");
			PathBuilder<?> entityCostPath = new PathBuilder(entityCostClass, "keywordCosts");
			Date fromDate =  DateTime.now().minusDays(8).withHourOfDay(0).withHourOfDay(0).withMinuteOfHour(0).toDate();
			Date toDate = DateTime.now().withHourOfDay(0).withHourOfDay(0).withMinuteOfHour(0).toDate();
			Predicate p3 = kwCosts.key.loadDate.after(fromDate);
			Predicate p4 = kwCosts.key.loadDate.before(toDate);
			for (PowerSearchNode powerSearchNode : query.getMetricNodes()) {
				 Predicate predicate = qKeyword.id.in(new JPASubQuery().from(kwCosts).where(p3).where(p4).groupBy(kwCosts.key.uniqueId).having(getMetricPredicate(entityCostPath,powerSearchNode,schemaInfo)).list(kwCosts.key.uniqueId) );
				if (!powerSearchNode.getBooleanMatch().isValue()) {
					predicate = predicate.not();
				}
				builder.and(predicate);
			}
		}
		
		JPAQuery where = jpaQuery.where(builder);
		List<Keyword> kList = where.list(qKeyword);
		for (Keyword keyword : kList) {
			System.out.println("keyword : " + keyword);
		}
		
		return kList;
	}

	private List<Predicate> getOrGroupPredicates(
			List<SchemaProperty> schemaInfo, PathBuilder<?> entityPath,
			QKeyword qKeyword, JPAQuery jpaQuery,
			PowerSearchNode powerSearchNode) {
		List<Predicate> orNodes = new ArrayList<Predicate>();
		for (PowerSearchNode node : powerSearchNode.getNodes()) {
			if (node.getProp().equals("LIST")) {
				QListId qListId = QListId.listId;
				jpaQuery.join(qKeyword.lists, qListId);
				orNodes.add(qListId.listName.contains(node.getVal()));
			} else if (node.getProp().equals("ANY TEXT")) {
				List<Predicate> anyPredicateList = getAnyPredicateList(schemaInfo, entityPath, node);
				orNodes.addAll(anyPredicateList);
			} else {
				Predicate predicate = getNodePredicate(entityPath, node, schemaInfo);
				orNodes.add(predicate);
			}
		}
		return orNodes;
	}

	private void processPowerSearchNode(List<SchemaProperty> schemaInfo,
			PathBuilder<?> entityPath, QKeyword qKeyword, JPAQuery jpaQuery,
			BooleanBuilder builder, PowerSearchNode powerSearchNode) {
		if (powerSearchNode.getProp().equals("LIST")) {
			QListId qListId = QListId.listId;
			jpaQuery.join(qKeyword.lists, qListId);
			Predicate predicate = qListId.listName.contains(powerSearchNode.getVal());
			builder.and(predicate);
		} else if (powerSearchNode.getProp().equals("ANY TEXT")) {
				List<Predicate> anyPredicateList = getAnyPredicateList(schemaInfo, entityPath, powerSearchNode);
				builder.andAnyOf(anyPredicateList.toArray(new Predicate[anyPredicateList.size()]));
		} else {
			Predicate predicate = getNodePredicate(entityPath, powerSearchNode, schemaInfo);
			builder.and(predicate);
		}
	}

	private Predicate getMetricPredicate(PathBuilder<?> entityPath, PowerSearchNode powerSearchNode, List<SchemaProperty> schemaInfo) {
		Predicate predicate = null;
		SchemaProperty schemProperty = getSchemaProp(powerSearchNode, schemaInfo);
		if (schemProperty.getPropType().equals("Long")) {
			NumberPath<Long> path = entityPath.getNumber(schemProperty.getPropColumn(), Long.class);
			if (powerSearchNode.getOp().getName().equals("EQUAL")) {
				predicate = path.sum().eq(Long.parseLong(powerSearchNode.getVal()));
			} else if (powerSearchNode.getOp().getName().equals("GREATER")) {
				predicate = path.sum().gt(Long.parseLong(powerSearchNode.getVal()));
			} else if (powerSearchNode.getOp().getName().equals("LESS")) {
				predicate = path.sum().lt(Long.parseLong(powerSearchNode.getVal()));
			}
		} else if (schemProperty.getPropType().equals("BigDecimal")) {
			NumberPath<BigDecimal> path = entityPath.getNumber(schemProperty.getPropColumn(), BigDecimal.class);
			if (powerSearchNode.getOp().getName().equals("EQUAL")) {
				predicate = path.sum().eq(new BigDecimal(powerSearchNode.getVal()));
			} else if (powerSearchNode.getOp().getName().equals("GREATER")) {
				predicate = path.sum().gt(new BigDecimal(powerSearchNode.getVal()));
			} else if (powerSearchNode.getOp().getName().equals("LESS")) {
				predicate = path.sum().lt(new BigDecimal(powerSearchNode.getVal()));
			}
		} 
		return predicate;
	}

	private List<Predicate> getAnyPredicateList(List<SchemaProperty> schemaInfo,
			PathBuilder<?> entityPath, PowerSearchNode powerSearchNode) {
		List<Predicate> ret = new ArrayList<Predicate>();
		for (SchemaProperty sProp : schemaInfo) {
			if (sProp.getPropLabel().equals("ANY TEXT") || sProp.getPropLabel().equals("IMPRESSIONS") || sProp.getPropLabel().equals("CLICKS") ||
					sProp.getPropLabel().equals("INQUIRIES") || sProp.getPropLabel().equals("REVENUE") || sProp.getPropLabel().equals("COST")
					|| sProp.getPropLabel().equals("NC") || sProp.getPropLabel().equals("LIST") ) {
				continue;
			}
			PowerSearchNode anyNode = new PowerSearchNode();
			anyNode.setBooleanMatch(powerSearchNode.getBooleanMatch());
			anyNode.setOp(powerSearchNode.getOp());
			anyNode.setVal(powerSearchNode.getVal());
			anyNode.setProp(sProp.getPropLabel());
			Predicate anyPredicate = getNodePredicate(entityPath, anyNode, schemaInfo);
			ret.add(anyPredicate);
		}
		return ret;
	}

	private Predicate getNodePredicate(PathBuilder<?> entityPath, PowerSearchNode powerSearchNode, List<SchemaProperty> schemaInfo) {
		Predicate predicate = null;
		StringPath path = getPropPath(entityPath, powerSearchNode, schemaInfo);
		if (powerSearchNode.getOp().getName().equals("EXACT")) {
			predicate = path.equalsIgnoreCase(powerSearchNode.getVal());
		}
		if (powerSearchNode.getOp().getName().equals("CONTAIN")) {
			predicate = path.containsIgnoreCase(powerSearchNode.getVal());
		}
		if (powerSearchNode.getOp().getName().equals("BEGIN")) {
			predicate = path.startsWithIgnoreCase(powerSearchNode.getVal());
		}
		if (powerSearchNode.getOp().getName().equals("END")) {
			predicate = path.endsWithIgnoreCase(powerSearchNode.getVal());
		}
		if (!powerSearchNode.getBooleanMatch().isValue()) {
			predicate = predicate.not();
		}
		return predicate;
	}

	private StringPath getPropPath(PathBuilder<?> entityPath, PowerSearchNode node, List<SchemaProperty> schemaInfoList) {
		SchemaProperty schemProperty = getSchemaProp(node, schemaInfoList);
		return entityPath.getString(schemProperty.getPropColumn());
	}
	
	private SchemaProperty getSchemaProp(PowerSearchNode node,
			List<SchemaProperty> schemaInfoList) {
		SchemaProperty schemProperty = null;
		for (SchemaProperty schemaInfo : schemaInfoList) {
			if (schemaInfo.getPropLabel().equals(node.getProp()) ) {
				schemProperty = schemaInfo;
				break;
			}
		}
		return schemProperty;
	}
	
}

@Entity
class Booking {
	
	@Id @GeneratedValue
	private Long id;
	private String bookingName;
	
	public Booking() { }
	public Booking(Long id, String bookingName) {
		super();
		this.id = id;
		this.bookingName = bookingName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBookingName() {
		return bookingName;
	}
	public void setBookingName(String bookingName) {
		this.bookingName = bookingName;
	}
	@Override
	public String toString() {
		return "Booking [id=" + id + ", bookingName=" + bookingName + "]";
	}
	
}

