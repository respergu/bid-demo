package demo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A composite key for use by the various cost entities.
 * <p>
 * <small>
 * $File: //depot/eng/bidprocessor/branches/bid2.0_autobid/bidprocessor-core/src/main/java/net/eim/bidprocessor/core/entities/ComponentCostsKey.java $ <br/>
 * $Change: 172026 $ submitted by $Author: respergue $ at $DateTime: 2014/04/23 13:18:22 $
 * </small>
 * </p>
 *
 * @author Collin Rodolitz
 * @version $Revision: #1 $
 **/
@Embeddable
public class ComponentCostsKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name="unique_id")
    private Long uniqueId;

    @Temporal(TemporalType.DATE)
    private Date loadDate;

    public ComponentCostsKey() { }

    public ComponentCostsKey(Long uniqueId, Date loadDate) {
        this.uniqueId = uniqueId;
        this.loadDate = loadDate;
    }

    public Long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Date getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(Date loadDate) {
        this.loadDate = loadDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((loadDate == null) ? 0 : loadDate.hashCode());
        result = prime * result
                + ((uniqueId == null) ? 0 : uniqueId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ComponentCostsKey other = (ComponentCostsKey) obj;
        if (loadDate == null) {
            if (other.loadDate != null) {
                return false;
            }
        } else if (!loadDate.equals(other.loadDate)) {
            return false;
        }
        if (uniqueId == null) {
            if (other.uniqueId != null) {
                return false;
            }
        } else if (!uniqueId.equals(other.uniqueId)) {
            return false;
        }
        return true;
    }

}
