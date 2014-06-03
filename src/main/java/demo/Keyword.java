package demo;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;


/**
 * Provide a detailed description of the class here.
 * <p>
 * <small>
 * $File: //depot/eng/bidprocessor/trunk/bidprocessor-core/src/main/java/net/eim/bidprocessor/core/entities/Keyword.java $ <br/>
 * $Change: 173578 $ submitted by $Author: respergue $ at $DateTime: 2014/05/06 16:35:54 $
 * </small>
 * </p>
 *
 * @author Collin Rodolitz
 * @version $Revision: #7 $
 **/
@Entity
@Table(name = "keywords")
public class Keyword extends SearchEngineComponent {

    private Long keywordId;

    private String keywordType;

    private String keyword;
	private String groupName;
    private String destinationUrl;
    private String status;
    private String engineName;
    private String vertical;
    private Integer qualityScore;
    private Integer rank;
    private Integer maxUserBid;
    
	private String sourceId;

    @Type(type="yes_no")
    private boolean scheduler;

    private BigDecimal minCpc;

    private BigDecimal maxCpc;

    private BigDecimal adjCpc;

    private Long groupId;
    
    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="unique_id")
    private List<ListId> lists;
    
    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="unique_id")
    private List<KeywordCosts> costs;

    @Type(type="yes_no")
    private boolean biddable;

    public Long getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Long keywordId) {
        this.keywordId = keywordId;
    }

    public String getKeywordType() {
        return keywordType;
    }

    public void setKeywordType(String keywordType) {
        this.keywordType = keywordType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public boolean getScheduler() {
        return scheduler;
    }

    public void setScheduler(boolean scheduler) {
        this.scheduler = scheduler;
    }

    public BigDecimal getMinCpc() {
        return minCpc;
    }

    public void setMinCpc(BigDecimal minCpc) {
        this.minCpc = minCpc;
    }

    public BigDecimal getMaxCpc() {
        return maxCpc;
    }

    public void setMaxCpc(BigDecimal maxCpc) {
        this.maxCpc = maxCpc;
    }

    public BigDecimal getAdjCpc() {
        return adjCpc;
    }

    public void setAdjCpc(BigDecimal adjCpc) {
        this.adjCpc = adjCpc;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public boolean isBiddable() {
        return biddable;
    }

    public void setBiddable(boolean biddable) {
        this.biddable = biddable;
    }
    
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDestinationUrl() {
		return destinationUrl;
	}

	public void setDestinationUrl(String destinationUrl) {
		this.destinationUrl = destinationUrl;
	}

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getEngineName() {
		return engineName;
	}

	public void setEngineName(String engineName) {
		this.engineName = engineName;
	}

	public String getVertical() {
		return vertical;
	}

	public void setVertical(String vertical) {
		this.vertical = vertical;
	}

	public Integer getQualityScore() {
		return qualityScore;
	}

	public void setQualityScore(Integer qualityScore) {
		this.qualityScore = qualityScore;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getMaxUserBid() {
		return maxUserBid;
	}

	public void setMaxUserBid(Integer maxUserBid) {
		this.maxUserBid = maxUserBid;
	}

	private List<ListId> getLists() {
		return lists;
	}

	private void setLists(List<ListId> lists) {
		this.lists = lists;
	}

	public List<KeywordCosts> getCosts() {
		return costs;
	}

	public void setCosts(List<KeywordCosts> costs) {
		this.costs = costs;
	}

	
}
