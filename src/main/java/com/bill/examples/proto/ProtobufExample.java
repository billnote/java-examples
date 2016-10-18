package com.bill.examples.proto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.bill.examples.proto.BidResponseBuilder.BidResponse;
import com.bill.examples.proto.TrafficForwardingLogBuilder.TrafficForwardingLog;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;

public class ProtobufExample {
	public static void main(String[] args) throws ParseException {
		BidResponse.Builder builder = BidResponseBuilder.BidResponse.newBuilder();
		builder.setId("100000001");
		builder.setBidid("10000222");
		builder.setCur("CNY");
		builder.setNbr(0);

		BidResponse.SeatBid.Builder seatBidBuilder = BidResponse.SeatBid.newBuilder();
		seatBidBuilder.setSeat("ABDCEFGG");

		BidResponse.SeatBid.Bid.Builder bidBuilder = BidResponse.SeatBid.Bid.newBuilder();
		bidBuilder.setId("BID0000001");
		bidBuilder.setImpid("IMP000001");
		bidBuilder.setPrice(10000l);
		bidBuilder.setAdid("ADID00001");
		bidBuilder.setNurl("http://www.mplusmedia.cn");
		bidBuilder.setAdm("ADM");
		bidBuilder.setBundle("com.mplus.test");
		bidBuilder.setCid("CID10001");
		bidBuilder.setCrid("CRID100003");

		seatBidBuilder.addBid(bidBuilder.build());
		builder.addSeatbid(seatBidBuilder.build());

		System.out.println(builder.build().toString());

		BidResponse.Builder builder2 = BidResponseBuilder.BidResponse.newBuilder();
		TextFormat.getParser().merge(builder.build().toString(), builder2);
		BidResponse response = builder2.build();

		System.out.println(response.getBidid());

		try (InputStream is = new FileInputStream(new File("/home/bill.huang/tmp/forwarding.201607271803.0"))) {
			while (true) {
				TrafficForwardingLog log = TrafficForwardingLog.parseDelimitedFrom(is);
				if (null == log) {
					break;
				} else {
					System.out.println(log);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
