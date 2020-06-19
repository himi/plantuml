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
package net.sourceforge.plantuml.version;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.command.PSystemSingleLineFactory;
import net.sourceforge.plantuml.security.SecurityProfile;
import net.sourceforge.plantuml.security.SecurityUtils;

public class PSystemVersionFactory extends PSystemSingleLineFactory {

	@Override
	protected AbstractPSystem executeLine(String line) {
		try {
			if (line.matches("(?i)^(authors?|about)\\s*$")) {
				return PSystemVersion.createShowAuthors();
			}
			if (line.matches("(?i)^version\\s*$")) {
				return PSystemVersion.createShowVersion();
			}
			if (line.matches("(?i)^stdlib\\s*$")) {
				return PSystemVersion.createStdLib();
			}
			if (SecurityUtils.getSecurityProfile() == SecurityProfile.UNSECURE && line.matches("(?i)^path\\s*$")) {
				return PSystemVersion.createPath();
			}
			if (line.matches("(?i)^testdot\\s*$")) {
				return PSystemVersion.createTestDot();
			}
			if (SecurityUtils.getSecurityProfile() == SecurityProfile.UNSECURE
					&& line.matches("(?i)^dumpstacktrace\\s*$")) {
				return PSystemVersion.createDumpStackTrace();
			}
			if (line.matches("(?i)^keydistributor\\s*$")) {
				return PSystemVersion.createKeyDistributor();
			}
			if (line.matches("(?i)^keygen\\s*$")) {
				line = line.trim();
				return new PSystemKeygen("");
			}
			if (line.matches("(?i)^keyimport(\\s+[0-9a-z]+)?\\s*$")) {
				line = line.trim();
				final String key = line.substring("keyimport".length()).trim();
				return new PSystemKeygen(key);
			}
			if (line.matches("(?i)^keycheck\\s+([0-9a-z]+)\\s+([0-9a-z]+)\\s*$")) {
				final Pattern p = Pattern.compile("(?i)^keycheck\\s+([0-9a-z]+)\\s+([0-9a-z]+)\\s*$");
				final Matcher m = p.matcher(line);
				if (m.find()) {
					return new PSystemKeycheck(m.group(1), m.group(2));
				}
			}
		} catch (IOException e) {
			Log.error("Error " + e);

		}
		return null;
	}
}
