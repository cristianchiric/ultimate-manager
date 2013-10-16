package restservice;

/*
 * This class is used in order to pack the messages;
 * 
 * `from` field represents the sender id;
 */

public class MessagePack {
	private Integer from;
	private String timestamp;
	private String msg;
	
	public MessagePack(Integer from, String timestamp, String msg){
		this.from = from;
		this.timestamp = timestamp;
		this.msg = msg;
	}
	
	public Integer getFrom() {
		return from;
	}
	public void setFrom(Integer from) {
		this.from = from;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}
