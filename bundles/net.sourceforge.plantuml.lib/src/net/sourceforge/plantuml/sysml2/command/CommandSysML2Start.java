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
 * Contributor: Hisashi Miyashita (himi@mgnite.com)
 */
package net.sourceforge.plantuml.sysml2.command;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.NamespaceStrategy;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sysml2.SysML2Diagram;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class CommandSysML2Start extends SingleLineCommand2<SysML2Diagram> {
	public CommandSysML2Start() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandSysML2Start.class.getName(),
                                 RegexLeaf.start(), //
                                 new RegexLeaf("rec"), //
                                 RegexLeaf.spaceOneOrMore(), //
                                 new RegexLeaf("UD", "(usage|def)"), //
                                 RegexLeaf.spaceOneOrMore(), //
                                 new RegexOr(new RegexConcat(new RegexLeaf("DISPLAY1", "[%g]([^%g]+)[%g]"), //
                                                             RegexLeaf.spaceOneOrMore(),
                                                             new RegexLeaf("as"),
                                                             RegexLeaf.spaceOneOrMore(),
                                                             new RegexLeaf("CODE1", "([\\p{L}0-9_.]+)")),
                                             new RegexLeaf("CODE2", "([\\p{L}0-9_.]+)"), //
                                             new RegexLeaf("CODE3", "[%g]([^%g]+)[%g]")),
                                 RegexLeaf.spaceZeroOrMore(), //
                                 new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
                                 RegexLeaf.spaceZeroOrMore(), //
                                 new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
                                 RegexLeaf.spaceZeroOrMore(), //
                                 color().getRegex(), //
                                 RegexLeaf.spaceZeroOrMore(), //
                                 new RegexOptional(new RegexLeaf("LINECOLOR", "##(?:\\[(dotted|dashed|bold)\\])?(\\w+)?")),
                                 new RegexLeaf("\\s*\\{"), RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(SysML2Diagram diagram, LineLocation location, RegexResult arg) {
		final IGroup currentPackage = diagram.getCurrentGroup();
		final String idShort = arg.getLazzy("CODE", 0);
		final Ident idNewLong = diagram.buildLeafIdentSpecial(idShort);
		final Code code = diagram.buildCode(idShort);

		String display = arg.getLazzy("DISPLAY", 0);
		if (display == null) {
			display = code.getName();
		}

        final GroupType groupType;
        if ("usage".equals(arg.get("UD", 0))) {
            groupType = GroupType.STATE;
        } else {
            groupType = GroupType.DEF;
        }
		diagram.gotoGroup(idNewLong, code, Display.getWithNewlines(display), groupType, currentPackage,
                          NamespaceStrategy.SINGLE);
		final IEntity p = diagram.getCurrentGroup();
		final String stereotype = arg.get("STEREOTYPE", 0);
		if (stereotype != null) {
			p.setStereotype(new Stereotype(stereotype));
		}
		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			p.addUrl(url);
		}

		Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());

		final HColor lineColor = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("LINECOLOR", 1));
		if (lineColor != null) {
			colors = colors.add(ColorType.LINE, lineColor);
		}
		if (arg.get("LINECOLOR", 0) != null) {
			colors = colors.addLegacyStroke(arg.get("LINECOLOR", 0));
		}
		p.setColors(colors);

		return CommandExecutionResult.ok();
	}

}
