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
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.sequencediagram.GroupingType;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class CommandGrouping extends SingleLineCommand2<SequenceDiagram> {

	public CommandGrouping() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandGrouping.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("PARALLEL", "(&[%s]*)?"), //
				new RegexLeaf("TYPE", "(opt|alt|loop|par|par2|break|critical|else|end|also|group)"), //
				new RegexLeaf("COLORS", "((?<!else)(?<!also)(?<!end)#\\w+)?(?:[%s]+(#\\w+))?"), //
				new RegexOptional(//
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("COMMENT", "(.*?)") //
						)), RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(SequenceDiagram diagram, LineLocation location, RegexResult arg) {
		String type = StringUtils.goLowerCase(arg.get("TYPE", 0));
		final HColor backColorElement = diagram.getSkinParam().getIHtmlColorSet()
				.getColorIfValid(arg.get("COLORS", 0));
		final HColor backColorGeneral = diagram.getSkinParam().getIHtmlColorSet()
				.getColorIfValid(arg.get("COLORS", 1), diagram.getSkinParam().getBackgroundColor());
		String comment = arg.get("COMMENT", 0);
		final GroupingType groupingType = GroupingType.getType(type);
		if ("group".equals(type)) {
			if (StringUtils.isEmpty(comment)) {
				comment = "group";
			} else {
				final Pattern p = Pattern.compile("^(.*?)\\[(.*)\\]$");
				final Matcher m = p.matcher(comment);
				if (m.find()) {
					type = m.group(1);
					comment = m.group(2);
				}
			}
		}

		final boolean parallel = arg.get("PARALLEL", 0) != null;
		final boolean result = diagram.grouping(type, comment, groupingType, backColorGeneral, backColorElement,
				parallel);
		if (result == false) {
			return CommandExecutionResult.error("Cannot create group");
		}
		return CommandExecutionResult.ok();
	}
}