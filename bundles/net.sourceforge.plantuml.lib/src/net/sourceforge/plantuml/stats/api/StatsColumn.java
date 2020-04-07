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
package net.sourceforge.plantuml.stats.api;

public enum StatsColumn {
	SESSION_ID("Session ID"), DIAGRAM_TYPE("Diagram type"), FORMAT("Format"), VERSION("Version"), STARTING("Starting"), LAST(
			"Last"), DURATION_STRING("Duration"), PARSED_COUNT("# Parsed"), PARSED_MEAN_TIME(
			"Mean parsing\\ntime (ms)"), PARSED_STANDARD_DEVIATION("Standard\\ndeviation (ms)"), PARSED_MAX_TIME(
			"Max parsing\\ntime (ms)"), GENERATED_COUNT("# Generated"), GENERATED_MEAN_TIME(
			"Mean generation\\ntime (ms)"), GENERATED_STANDARD_DEVIATION("Standard\\ndeviation (ms)"), GENERATED_MAX_TIME(
			"Max generation\\ntime (ms)");

	private final String title;

	private StatsColumn(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}
