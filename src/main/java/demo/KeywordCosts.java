package demo;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "keyword_costs")
public class KeywordCosts extends ComponentCosts {

	private BigDecimal maxCpc;

    private BigDecimal costMicros;

    private BigDecimal minCpc;

    private BigDecimal qualityScore;

    public BigDecimal getMaxCpc() {
        return maxCpc;
    }

    public void setMaxCpc(BigDecimal maxCpc) {
        this.maxCpc = maxCpc;
    }

    public BigDecimal getCostMicros() {
        return costMicros;
    }

    public void setCostMicros(BigDecimal costMicros) {
        this.costMicros = costMicros;
    }

    public BigDecimal getMinCpc() {
        return minCpc;
    }

    public void setMinCpc(BigDecimal minCpc) {
        this.minCpc = minCpc;
    }

    public BigDecimal getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(BigDecimal qualityScore) {
        this.qualityScore = qualityScore;
    }

    @Override
	public String toString() {
		return "KeywordCosts [getClicks()=" + getClicks()
				+ ", getImpressions()=" + getImpressions()
				+ ", getAvgPosition()=" + getAvgPosition()
				+ ", getCostDollars()=" + getCostDollars() + ", getRevenue()="
				+ getRevenue() + ", getInquiries()=" + getInquiries()
				+ ", getNetContribution()=" + getNetContribution() + "]";
	}
    
}