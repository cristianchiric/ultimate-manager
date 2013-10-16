package restservice;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import com.google.gson.Gson;

@Singleton
@Path("rest")
public class RestService {

	/*
	 * This function stores unread messages in a HashMap.
	 * The HashMap key represents the recipient.
	 * The mapped values of the HashMap represents MessagePacks - contains the sender, timestamp and the message.
	 */
	private HashMap<Integer, List<MessagePack>> messages = new HashMap<>();

	@GET
	@Produces("application/json; charset=UTF-8")
	// This functions responds to /reso/rest/sendMsg path
	// Example : http://localhost:8080/RESTfulChat/reso/rest/sendMsg?from=1&to=2&msg=HelloWorld&key=123
	@Path("sendMsg")
	public String sendMsg(@QueryParam("from") Integer from, @QueryParam("to") Integer to, @QueryParam("msg") String msg, @QueryParam("key") String key) throws IOException {

		if(key!=null && from !=null && to !=null) { // if key is valid (from cookie for example) and the name exists and to field exists
				List<MessagePack> individualList= messages.get(to);
	
				//retrives current moment;
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
	
				//adds the message pack
				if(individualList!=null){
					individualList.add(new MessagePack(from, dateFormat.format(date), msg));
				} else {
					individualList = new ArrayList<MessagePack>();
					individualList.add(new MessagePack(from, dateFormat.format(date), msg));
					messages.put(to, individualList);
				}
		}

		return "MsgSent"; //else => error code/msg
	}

	/*
	 * This function retrieves all the unread messages for a certain user.
	 * Example : http://localhost:8080/RESTfulChat/reso/rest/getMsgs?me=2&key=456 
	 */


	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("getMsgs")
	public String getMsgs(@QueryParam("me") Integer me, @QueryParam("key") String key) throws IOException {

		List<MessagePack> individualList=null;

		if(key!=null && me !=null) { // if key is valid (from cookie for example) and the name exists
			individualList = messages.get(me);
			// after the messages are retrieved by user, all messages should be added to Message History
		}

		//converts Java Object to json
		Gson gson = new Gson();
		String response = gson.toJson(individualList);

		return response; //else => 4xx 5xx error code/msg
	}
}
