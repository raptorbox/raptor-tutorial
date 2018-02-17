package createnet.raptorbox.quickstart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.createnet.raptor.models.app.App;
import org.createnet.raptor.models.app.AppRole;
import org.createnet.raptor.models.auth.User;
import org.createnet.raptor.models.objects.Device;
import org.createnet.raptor.models.objects.Stream;
import org.createnet.raptor.models.query.AppQuery;
import org.createnet.raptor.models.query.DeviceQuery;
import org.createnet.raptor.sdk.PageResponse;
import org.createnet.raptor.sdk.Raptor;

import com.mongodb.util.Hash;

import createnet.raptorbox.quickstart.Utils.Raptorbox;

/**
 * Hello world!
 *
 */
public class RaptorTutorial {

	static final String appProperties = "application.properties";

	public static void main(String[] args) {

		RaptorTutorial quickStart = new RaptorTutorial();

		// Login
		Raptor raptor = Raptorbox.getRaptor();

		System.out.println("username: " + raptor.Auth().getUser().getUsername());

		// Create device
		Device dev = quickStart.createDevice();
		System.out.println("Device Id: " + dev.id());

		// Add stream to above created device
		quickStart.addStream(dev);

		// Add channels into the new stream
		quickStart.addChannelsToStream(dev);

		// Update stream
		quickStart.updateStream(dev);

		// Load device with deviceId
		quickStart.loadDevice(dev.id());

		// Update device
		quickStart.updateDevice(dev);

		// List down created devices
		quickStart.list();

		// Search devices by device name
		quickStart.searchByName(dev);

		// Search device by user id
		quickStart.searchByUserId(dev);

		// records
		Records records = new Records(dev);
		Stream stream = records.addStream();

		// Push record in the device
		records.pushData(stream);

		// Pull records
		records.pullRecords(stream);

		// Pull last record
		records.pullLastUpdate(stream);

		// Drop records
		records.dropData(stream);

		// App tutorial
		quickStart.appTutorial(raptor);

		// Create user with default role
		quickStart.createUserWithDefaultRole(raptor);
		
		// Search and delete the user
		quickStart.searchAndDeleteUser(raptor);

		// Create user with owner Id
		quickStart.createUserWithOwnerId(raptor.Auth().getUser().getId(), raptor);
	}

	// create device
	public Device createDevice() {
		Raptor raptor = Raptorbox.getRaptor();

		Device dev = new Device();
		dev.name("test_dev");

		// validating device
		dev.validate();
		raptor.Inventory().create(dev);

		return dev;
	}

	// load device
	public void loadDevice(String deviceId) {
		Raptor raptor = Raptorbox.getRaptor();

		Device dev = raptor.Inventory().load(deviceId);

		dev.toString();
	}

	// adding stream to already created device
	public void addStream(Device device) {
		Raptor raptor = Raptorbox.getRaptor();

		// add default stream
		String streamName = "test_stream";

		// add channel to stream
		String channelName = "channel_1";
		String channelType = "string";

		device.addStream(streamName, channelName, channelType);

		raptor.Inventory().update(device);
	}

	// add new stream and channels into the new Stream
	public void addChannelsToStream(Device device) {
		Raptor raptor = Raptorbox.getRaptor();

		String streamName = "new_test_stream";
		Stream stream = device.addStream(streamName);

		String channelName = "test_channel_1";
		String channelType = "string"; // only 'number', 'string' and 'boolean' are allowed
		stream.addChannel(channelName, channelType);

		String otherChannel = "test_channel_2";
		String otherChannelType = "string"; // only 'number', 'string' and 'boolean' are allowed
		stream.addChannel(otherChannel, otherChannelType);

		// validating stream
		stream.validate();

		device.addStream(streamName, channelName, channelType);
		device.validate();
		raptor.Inventory().update(device);
	}

	// Updating device, add extra information in device
	public void updateDevice(Device device) {
		Raptor raptor = Raptorbox.getRaptor();

		device.name("test device");

		// properties can be used to add extra data/information in device as key-value
		// pairs
		String propertyKey = "foo";
		String propertyValue = "bar";
		device.properties().put(propertyKey, propertyValue);

		raptor.Inventory().update(device);
	}

	// Update already created stream
	public void updateStream(Device device) {
		Raptor raptor = Raptorbox.getRaptor();

		Stream stream = device.stream("test_stream");
		stream.addChannel("new channel", "string");

		raptor.Inventory().update(device);
	}

	// list devices
	public void list() {
		Raptor raptor = Raptorbox.getRaptor();
		PageResponse<Device> list = raptor.Inventory().list();

		System.out.println(list.getTotalElements());
	}

	// search device by name
	public void searchByName(Device device) {

		Raptor raptor = Raptorbox.getRaptor();

		DeviceQuery q = new DeviceQuery();
		q.name.contains("test");
		PageResponse<Device> results = raptor.Inventory().search(q);

		if (results != null && results.getTotalElements() > 0) {
			Device dev = results.getContent().get(0);
			System.out.println(dev.toJSON().toString());
		}

	}

	// search devices by userId
	public void searchByUserId(Device device) {

		Raptor raptor = Raptorbox.getRaptor();

		String userId = raptor.Auth().getUser().getId().toString();

		DeviceQuery q = new DeviceQuery();
		q.userId(userId);

		PageResponse<Device> results = raptor.Inventory().search(q);

		if (results != null && results.getTotalElements() > 0) {
			Device dev = results.getContent().get(0);
			System.out.println(dev.getUserId());
		}

	}

	public void appTutorial(Raptor raptor) {
		// Create User
		User u = new User();
		u.setUsername("test_user");
		u.setPassword("test_user");
		u.setEmail("test_user" + "@test.raptor.local");
		u.setOwnerId(raptor.Auth().getUser().getId());
		User user = raptor.Admin().User().create(u);
		System.out.println("Created user test_user1 : test_user1 with uuid " + user.getId());

		// List application owned by user or belong to user
		PageResponse<App> pager = raptor.App().list();
		List<App> list = pager.getContent();
		System.out.println("found apps " + list.size());

		// Create an app
		App app = new App();
		app.setName("test_" + System.currentTimeMillis());
		app.setUserId(raptor.Auth().getUser().getId());
		App a = raptor.App().create(app);

		// Update app, add users and roles to the app
		List<String> permissions = new ArrayList<>();
		permissions.add("admin_device");
		permissions.add("read_stream");
		AppRole role1 = new AppRole();
		role1.setName("role1");
		role1.addPermissions(permissions);

		List<String> permissions2 = new ArrayList<>();
		permissions2.add("read_user");
		AppRole role2 = new AppRole();
		role2.setName("role2");
		role2.addPermissions(permissions2);
		a.addRole(role1);
		a.addRole(role2);
		a.addUser(user, role1.getName());
		raptor.App().update(a);

		// addDevice(app.getId(), raptor);

		// read an application
		App application = raptor.App().load(app.getId());
		System.out.println("Application " + application.getName());

		// delete the application
		raptor.App().delete(a);
		System.out.println("Application deleted");

		// delete test user
		raptor.Admin().User().delete(user.getId(), raptor.Auth().getUser().getId());
		System.out.println("Test user deleted");

		AppQuery appQuery = new AppQuery();
		appQuery.name.contains("test");
		PageResponse<App> pageResponse = raptor.App().search(appQuery);

		System.out.println("Applications found " + pageResponse.getTotalElements());
		pageResponse.getContent().forEach(item -> System.out.println("Application: " + item.getName()));

	}

	public void addDevice(String appId, Raptor raptor) {

		Device dev = new Device();
		dev.name("test_dev");
		dev.domain(appId);

		// validating device
		dev.validate();
		raptor.Inventory().create(dev);
	}

	public void createUserWithOwnerId(String id, Raptor raptor) {
		User u = new User();
		u.setUsername("test_user1");
		u.setPassword("test_user1");
		u.setEmail("test_user1" + "@test.raptor.local");
		u.setOwnerId(id);

		User user = raptor.Admin().User().create(u);
		System.out.println(
				"Created user test_user : test_user with uuid " + user.getId() + " ownerId: " + user.getOwnerId());
	}

	public void createUserWithDefaultRole(Raptor raptor) {
		User newUser = new User();
		newUser.setUsername("test_user_with_roles");
		newUser.setEmail("test_user_with_roles@raptor.local");
		newUser.setPassword("test_user_with_roles");
		newUser.setOwnerId(raptor.Auth().getUser().getId());
		raptor.Admin().User().create(newUser);
	}
	
	public void searchAndDeleteUser(Raptor raptor) {
		Map<String, Object> page = new HashMap<>();
		page.put("page", 1);
		page.put("size", 25);
		Map<String, Object> query = new HashMap<>();
		query.put("username", "test_user_with_roles");
		PageResponse<User> list = raptor.Admin().User().list(query, page);
		for(User u: list.getContent()) {
			if(u.getUsername().equals("test_user_with_roles")) {
				raptor.Admin().User().delete(u.getId());
			}
		}
	}

}
