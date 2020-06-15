/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC
 * LICENSE ("AGREEMENT"). [Eclipse Public License - v 1.0]
 * 
 * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES
 * RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 * 
 * You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 *
 * Original Author:  Arnaud Roques
 */
package net.sourceforge.plantuml.ftp;

// server

// FtpServer.java
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sourceforge.plantuml.FileFormat;

public class FtpServer {

	private final Map<String, FtpConnexion> datas = new TreeMap<String, FtpConnexion>();
	private final ExecutorService exeImage = Executors.newFixedThreadPool(2);
	private final String charset = "UTF-8";

	private final int listenPort;

	private int portFree = 10042;
	private String ip;
	private final FileFormat defaultfileFormat;

	public FtpServer(int listenPort, FileFormat defaultfileFormat) {
		this.listenPort = listenPort;
		this.defaultfileFormat = defaultfileFormat == null ? FileFormat.PNG : defaultfileFormat;
	}

	public synchronized int getFreePort() {
		portFree++;
		// Log.println("port=" + portFree);
		return portFree;
	}

	public void go() throws IOException {
		final ServerSocket s = new ServerSocket(listenPort);
		final ExecutorService exe = Executors.newCachedThreadPool();
		while (true) {
			final Socket incoming = s.accept();
			ip = incoming.getLocalAddress().getHostAddress();
			System.out.println("New Client Connected from " + incoming.getInetAddress().getHostName() + "... ");
			exe.submit(new FtpLoop(incoming, this));
		}
	}

	public String getIpServer() {
		return ip;
	}

	public synchronized FtpConnexion getFtpConnexion(String user) {
		if (user == null) {
			throw new IllegalArgumentException();
		}
		FtpConnexion data = datas.get(user);
		if (data == null) {
			data = new FtpConnexion(user, defaultfileFormat);
			datas.put(user, data);
		}
		return data;
	}

	public static void main(String[] args) throws IOException {
		System.out.println("****************************** ************************************************** ");
		System.out.println("****************************** FTP SERVER***********************************");

		System.out.println("****************************** ************************************************** ");
		System.out.println("Server Started...");
		System.out.println("Waiting for connections...");
		System.out.println(" ");
		new FtpServer(4242, FileFormat.PNG).go();
	}

	public void processImage(final FtpConnexion connexion, final String name) {
		exeImage.submit(new Runnable() {
			public void run() {
				try {
					connexion.processImage(name);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public final String getCharset() {
		return charset;
	}

}
