package createnet.raptorbox.quickstart;

import java.util.ArrayList;
import java.util.List;

import org.createnet.raptor.models.objects.RecordSet;
import org.createnet.raptor.models.objects.Device;
import org.createnet.raptor.models.objects.Stream;
import org.createnet.raptor.sdk.PageResponse;
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

		List<RecordSet> records = new ArrayList<RecordSet>();
		for (int i = 0; i < length; i++) {
			RecordSet r = new RecordSet(stream).channel("number", i).channel("string", "Hello world " + i).channel("boolean", true)
					.location(new GeoJsonPoint(11.45, 45.11));
			try {
				System.out.println(r.toJson().toString());
				
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

		PageResponse<RecordSet> bResults = raptor.Stream().pull(s);
		System.out.println("Before pushing records: " + bResults.getTotalElements());
		
		createRecordSet(s, 10);
		
		PageResponse<RecordSet> results = raptor.Stream().pull(s);
		System.out.println("After pushing records: " + results.getTotalElements());

		raptor.Stream().delete(s);

		PageResponse<RecordSet> dResults = raptor.Stream().pull(s);
		System.out.println("After deleting records: " + dResults.getTotalElements());
	}

	public void pullRecords(Stream s) {

		Raptor raptor = Raptorbox.getRaptor();

		int qt = 5;
		createRecordSet(s, qt);

		PageResponse<RecordSet> results = raptor.Stream().pull(s);
		System.out.println("Pull records: " + results.getTotalElements());
	}

	public void pullLastUpdate(Stream s) {

		Raptor raptor = Raptorbox.getRaptor();

		RecordSet record = raptor.Stream().lastUpdate(s);

		System.out.println(record.toJson().toString());
	}
}
