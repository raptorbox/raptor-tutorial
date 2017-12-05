package createnet.raptorbox.quickstart;

import java.util.List;

import org.createnet.raptor.models.objects.Device;
import org.createnet.raptor.models.objects.Stream;
import org.createnet.raptor.models.query.DeviceQuery;
import org.createnet.raptor.sdk.Raptor;

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
		List<Device> list = raptor.Inventory().list();

		System.out.println(list.size());
	}

	// search device by name
	public void searchByName(Device device) {

		Raptor raptor = Raptorbox.getRaptor();

		DeviceQuery q = new DeviceQuery();
		q.name.contains("test");
		List<Device> results = raptor.Inventory().search(q);

		if (results != null && results.size() > 0) {
			Device dev = results.get(0);
			System.out.println(dev.toJSON().toString());
		}

	}

	// search devices by userId
	public void searchByUserId(Device device) {

		Raptor raptor = Raptorbox.getRaptor();

		String userId = raptor.Auth().getUser().getId().toString();

		DeviceQuery q = new DeviceQuery();
		q.userId(userId);

		List<Device> results = raptor.Inventory().search(q);

		if (results != null && results.size() > 0) {
			Device dev = results.get(0);
			System.out.println(dev.getUserId());
		}

	}

}
