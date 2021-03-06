package com.akamai.qtip;

import java.net.URI;
import java.net.URISyntaxException;

import com.akamai.qtip.mqtt.iec.Jurisdiction;

public final class Broker {

	static public URI getURI(Jurisdiction jurisdiction) throws URISyntaxException {
		String uri = String.format("ssl://qtip-%s.a2s.ninja:8883", jurisdiction.toString());
		return new URI(uri);
	}

}
