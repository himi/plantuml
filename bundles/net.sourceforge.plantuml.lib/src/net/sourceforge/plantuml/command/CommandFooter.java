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
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;

public class CommandFooter extends SingleLineCommand2<TitledDiagram> {

	public CommandFooter() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandFooter.class.getName(), //
				RegexLeaf.start(), //
				new RegexOptional(new RegexLeaf("POSITION", "(left|right|center)")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("footer"), //
				new RegexOr( //
						new RegexConcat(RegexLeaf.spaceZeroOrMore(), new RegexLeaf(":"), RegexLeaf.spaceZeroOrMore()), //
						RegexLeaf.spaceOneOrMore()), //
				new RegexLeaf("LABEL", "(.*[\\p{L}0-9_.].*)"), RegexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(TitledDiagram diagram, LineLocation location, RegexResult arg) {
		final String align = arg.get("POSITION", 0);
		HorizontalAlignment ha = HorizontalAlignment.fromString(align, HorizontalAlignment.CENTER);
		if (SkinParam.USE_STYLES() && align == null) {
			ha = FontParam.FOOTER.getStyleDefinition(null)
					.getMergedStyle(((UmlDiagram) diagram).getSkinParam().getCurrentStyleBuilder())
					.getHorizontalAlignment();
		}
		diagram.getFooter().putDisplay(Display.getWithNewlines(arg.get("LABEL", 0)), ha);

		
		return CommandExecutionResult.ok();
	}
}
