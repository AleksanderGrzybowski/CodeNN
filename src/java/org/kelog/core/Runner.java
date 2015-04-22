package org.kelog.core;

import org.kelog.end.Client;

public class Runner {
	public static void main(String[] args) {

		Client client = new Client();
		System.out.println(client.match("int main(void)"));

	}
}