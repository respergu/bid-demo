package demo;

import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

/**
 * Provide a detailed description of the class here.
 * <p>
 * <small>
 * $File: //depot/eng/bidprocessor/branches/bid2.0_autobid/bidprocessor-core/src/main/java/net/eim/bidprocessor/core/entities/ComponentCosts.java $ <br/>
 * $Change: 173043 $ submitted by $Author: respergue $ at $DateTime: 2014/05/01 17:35:28 $
 * </small>
 * </p>
 *
 * @author Collin Rodolitz
 * @version $Revision: #3 $
 **/
@MappedSuperclass
public abstract class ComponentCosts {

    @EmbeddedId
    private ComponentCostsKey key;

    private Integer clicks;

    private Long impressions;

    private BigDecimal avgPosition;

    private BigDecimal costDollars;

    private BigDecimal revenue;
  
    private Long inquiries;

    private BigDecimal netContribution;

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    public Long getImpressions() {
        return impressions;
    }

    public void setImpressions(Long impressions) {
        this.impressions = impressions;
    }

    public BigDecimal getAvgPosition() {
        return avgPosition;
    }

    public void setAvgPosition(BigDecimal avgPosition) {
        this.avgPosition = avgPosition;
    }

    public BigDecimal getCostDollars() {
        return costDollars;
    }

    public void setCostDollars(BigDecimal costDollars) {
        this.costDollars = costDollars;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public Long getInquiries() {
        return inquiries;
    }

    public void setInquiries(Long inquiries) {
        this.inquiries = inquiries;
    }

    public BigDecimal getNetContribution() {
        return netContribution;
    }

    public void setNetContribution(BigDecimal netContribution) {
        this.netContribution = netContribution;
    }

	public ComponentCostsKey getKey() {
		return key;
	}
	
}
