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
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;

public class CommandMultilinesFooter extends CommandMultilines<TitledDiagram> {

	public CommandMultilinesFooter() {
		super("(?i)^(?:(left|right|center)?[%s]*)footer$");
	}

	@Override
	public String getPatternEnd() {
		return "(?i)^end[%s]?footer$";
	}

	public CommandExecutionResult execute(final TitledDiagram diagram, BlocLines lines) {
		lines = lines.trim(false);
		final Matcher2 m = getStartingPattern().matcher(lines.getFirst499().getTrimmed().getString());
		if (m.find() == false) {
			throw new IllegalStateException();
		}
		final String align = m.group(1);
		lines = lines.subExtract(1, 1);
		final Display strings = lines.toDisplay();
		if (strings.size() > 0) {
			HorizontalAlignment ha = HorizontalAlignment.fromString(align, HorizontalAlignment.CENTER);
			if (SkinParam.USE_STYLES() && align == null) {
				ha = FontParam.FOOTER.getStyleDefinition()
						.getMergedStyle(((UmlDiagram) diagram).getSkinParam().getCurrentStyleBuilder())
						.getHorizontalAlignment();
			}
			diagram.getFooter().putDisplay(strings, ha);
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Empty footer");
	}

}
