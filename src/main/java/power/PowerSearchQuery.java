package power;

import java.util.List;

public class PowerSearchQuery {

    private List<SchemaProperty> schemaInfo;
	private String className;
//	private String op;
	private Operator op;
	private List<PowerSearchNode> nodes;
	private List<PowerSearchNode> metricNodes;
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
//	public String getOp() {
//		return op;
//	}
//	public void setOp(String op) {
//		this.op = op;
//	}
	public List<PowerSearchNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<PowerSearchNode> nodes) {
		this.nodes = nodes;
	}

	public List<SchemaProperty> getSchemaInfo() {
		return schemaInfo;
	}
	public void setSchemaInfo(List<SchemaProperty> schemaInfo) {
		this.schemaInfo = schemaInfo;
	}
	
	public String toString() {
		return "PowerSearchQuery [className=" + className + ", op=" + getOp()
				+ ", nodes=" + nodes + "]";
	}
	public List<PowerSearchNode> getMetricNodes() {
		return metricNodes;
	}
	public void setMetricNodes(List<PowerSearchNode> metricNodes) {
		this.metricNodes = metricNodes;
	}
	public Operator getOp() {
		return op;
	}
	public void setOp(Operator op) {
		this.op = op;
	}
}
