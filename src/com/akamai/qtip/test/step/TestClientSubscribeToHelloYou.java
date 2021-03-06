package com.akamai.qtip.test.step;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Before;
import org.junit.Test;

import com.akamai.qtip.Broker;
import com.akamai.qtip.jwt.IECJWTBuilder;
import com.akamai.qtip.mqtt.iec.ClientBuilder;
import com.akamai.qtip.mqtt.iec.Jurisdiction;

public class TestClientSubscribeToHelloYou extends MyTestParameters {

	@Before
	public void before() throws Exception {

	}

	@Test
	public void test() {
		String jwt = null;

		IECJWTBuilder jwtBuilder = new IECJWTBuilder();
		try {
			jwt = jwtBuilder
					.setAuthGroups(this.getClientAuthGroup())
					.setClientId(this.getClientID())
					.setSigningKey(this.getKeyPrivate())
					.build();

			// callback functions defined, when certain MQTT events take place
			MqttCallback callback = new MqttCallback() {
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					System.out.println("MESSAGE FROM " + topic + ": " + new String(message.getPayload())
							+ " with this qos: " + Integer.toString(message.getQos()));
					System.exit(0);
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
				}

				@Override
				public void connectionLost(Throwable cause) {
					System.out.println("Exception " + cause);
					System.exit(0);
				}
			};

			ClientBuilder clientBuilder = new ClientBuilder();
			MqttClient client;
			try {
				client = clientBuilder
						.addAuthGroups(this.getClientAuthGroup())
						.setCallback(callback)
						.setClientId(this.getClientID())
						.setBrokerURI(Broker.getURI(Jurisdiction.US))
						.setJWT(jwt)
						.build();
				client.connect();
				client.subscribe(this.getTopicHelloYou());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("jwt: " + jwt);

		assertNotNull("Verify that JWT not null", jwt);
	}

}
