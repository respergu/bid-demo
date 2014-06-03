package power;

import java.util.ArrayList;
import java.util.List;

public class PowerSearchNode {
	

	private String prop;
	private Operator op;
	private BooleanMatch booleanMatch;
	private boolean show;
	private String val;
	private Integer nodeType;
	private List<PowerSearchNode> nodes = new ArrayList<PowerSearchNode>();
	
	public PowerSearchNode() {}
	public String getProp() {
		return prop;
	}
	public void setProp(String prop) {
		this.prop = prop;
	}
	public Operator getOp() {
		return op;
	}
	public void setOp(Operator op) {
		this.op = op;
	}
	public BooleanMatch getBooleanMatch() {
		return booleanMatch;
	}
	public void setBooleanMatch(BooleanMatch booleanMatch) {
		this.booleanMatch = booleanMatch;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	@Override
	public String toString() {
		return "PowerSearchNode [prop=" + prop + ", op=" + op
				+ ", booleanMatch=" + booleanMatch + ", show=" + show
				+ ", val=" + val + "]";
	}
	public Integer getNodeType() {
		return nodeType;
	}
	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;
	}
	public List<PowerSearchNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<PowerSearchNode> nodes) {
		this.nodes = nodes;
	}

	
	
}
