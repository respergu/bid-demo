package demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Provide a detailed description of the class here.
 * <p>
 * <small>
 * $File: //depot/eng/bidprocessor/trunk/bidprocessor-core/src/main/java/net/eim/bidprocessor/core/entities/SearchEngineComponent.java $ <br/>
 * $Change: 173424 $ submitted by $Author: respergue $ at $DateTime: 2014/05/05 14:52:16 $
 * </small>
 * </p>
 *
 * @author Collin Rodolitz
 * @version $Revision: #6 $
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class SearchEngineComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "unique_id")
    private Long id;

    private String account;

    private String campaignName;

    private Long campaignId;

    private Integer engineId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getEngineId() {
        return engineId;
    }

    public void setEngineId(Integer engineId) {
        this.engineId = engineId;
    }


}
