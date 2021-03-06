package createnet.raptorbox.quickstart;

import java.util.ArrayList;
import java.util.List;

import org.createnet.raptor.models.data.RecordSet;
import org.createnet.raptor.models.data.ResultSet;
import org.createnet.raptor.models.objects.Device;
import org.createnet.raptor.models.objects.Stream;
import org.createnet.raptor.sdk.Raptor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import createnet.raptorbox.quickstart.Utils.Raptorbox;

public class Records {

	private Device device;

	public Records(Device dev) {
		device = dev;
	}

	public Stream addStream() {
		Raptor raptor = Raptorbox.getRaptor();
		
		Stream s = device.addStream("test", "string", "string");
		device.addStream("test", "number", "number");
		device.addStream("test", "boolean", "boolean");
		
		raptor.Inventory().update(device);

		return s;
	}

	private List<RecordSet> createRecordSet(Stream stream, int length) {
		
		Raptor raptor = Raptorbox.getRaptor();

		List<RecordSet> records = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			
			RecordSet r = new RecordSet(stream).channel("number", i).channel("string", "Hello world " + i).channel("boolean", true)
					.location(new GeoJsonPoint(11.45, 45.11));
			System.out.println(r.toJson().toString());
			
			try {
				Thread.sleep(1000*2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			raptor.Stream().push(r);
		}

		return records;
	}

	public void pushData(Stream s) {

		createRecordSet(s, 1);
	}

	public void dropData(Stream s) {

		Raptor raptor = Raptorbox.getRaptor();

		ResultSet bResults = raptor.Stream().pull(s);
		System.out.println("Before pushing records: " + bResults.size());
		
		createRecordSet(s, 10);
		
		ResultSet results = raptor.Stream().pull(s);
		System.out.println("After pushing records: " + results.size());

		raptor.Stream().delete(s);

		ResultSet dResults = raptor.Stream().pull(s);
		System.out.println("After deleting records: " + dResults.size());
	}

	public void pullRecords(Stream s) {

		Raptor raptor = Raptorbox.getRaptor();

		int qt = 5;
		createRecordSet(s, qt);

		ResultSet results = raptor.Stream().pull(s);
		System.out.println("Pull records: " + results.size());
	}

	public void pullLastUpdate(Stream s) {

		Raptor raptor = Raptorbox.getRaptor();

		RecordSet record = raptor.Stream().lastUpdate(s);

		System.out.println(record.toJson().toString());
	}
}
