package com.bill.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CallCommandExample {
	static class StdReader implements Runnable {
		final InputStream in;

		StdReader(InputStream in) {
			this.in = in;
		}

		@Override
		public void run() {
			try (InputStreamReader stdin = new InputStreamReader(in); BufferedReader read = new BufferedReader(stdin)) {
				String line1 = null;
				while (true) {
					while ((line1 = read.readLine()) != null) {
						// do something
						if (line1 != null) {
							System.out.println(read.readLine());
						}
					}

					Thread.sleep(100);
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			Process process = Runtime.getRuntime().exec("/home/bill.huang/tmp/test-java-process.sh");
			try (InputStream stdin = process.getInputStream()) {
				StdReader reader = new StdReader(stdin);
				// 开启一个线程读取子进程的标准输入流
				new Thread(reader).start();
				process.waitFor();
				System.out.println(process.exitValue());
			}
			process.destroy();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
