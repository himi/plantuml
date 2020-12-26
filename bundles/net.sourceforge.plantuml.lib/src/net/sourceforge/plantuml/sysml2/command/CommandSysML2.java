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

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.FontParam;
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
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sysml2.SysML2Diagram;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class CommandSysML2 extends SingleLineCommand2<SysML2Diagram> {
	public CommandSysML2() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandSysML2.class.getName(),
                                 RegexLeaf.start(), //
                                 new RegexLeaf("TYPE", "(choice|fork|join|end|state|desc|port|portin|portout|usage|def)"), //
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
                                 new RegexOptional(new RegexLeaf("LINECOLOR", "##(?:\\[(dotted|dashed|bold)\\])?(\\w+)?")), //
                                 RegexLeaf.spaceZeroOrMore(), //
                                 new RegexOptional(new RegexConcat(new RegexLeaf(":"), //
                                                                   RegexLeaf.spaceZeroOrMore(), //
                                                                   new RegexLeaf("ADDFIELD", "(.*)") //
                                                                   )), RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

    private static Map<String, LeafType> initLeafTypeMap() {
        Map<String, LeafType> m = new HashMap<String, LeafType>();
        m.put("state", LeafType.STATE);
        m.put("desc", LeafType.STATE);
        m.put("usage", LeafType.USAGE);
        m.put("def", LeafType.REC_DEF);
        m.put("port", LeafType.PORT);
        m.put("portin", LeafType.PORTIN);
        m.put("portout", LeafType.PORTOUT);
        m.put("choice", LeafType.STATE_CHOICE);
        m.put("fork", LeafType.STATE_FORK_JOIN);
        m.put("join", LeafType.STATE_FORK_JOIN);
        m.put("end", LeafType.CIRCLE_END);
        return m;
    }

    private static final Map<String, LeafType> leafTypeMap = initLeafTypeMap();

	private LeafType getLeafType(String type) {
        type = type.toLowerCase();
        return leafTypeMap.get(type);
	}

	@Override
	protected CommandExecutionResult executeArg(SysML2Diagram diagram, LineLocation location, RegexResult arg) {
		final String idShort = arg.getLazzy("CODE", 0);
		final Ident ident = diagram.buildLeafIdent(idShort);
		final Code code = diagram.buildCode(idShort);
		String display = arg.getLazzy("DISPLAY", 0);
		if (display == null) {
			display = code.getName();
		}

        final String typeStr = arg.get("TYPE", 0);
        final LeafType type = getLeafType(typeStr);

		final String stereotype = arg.get("STEREOTYPE", 0);

		if (diagram.checkConcurrentStateOk(ident, code) == false) {
			return CommandExecutionResult.error("The state " + code.getName()
					+ " has been created in a concurrent state : it cannot be used here.");
		}

        IEntity ent;
        if ("desc".equals(typeStr)) {
        	ent = diagram.getGroup(code);
        	if (ent == null) {
        		ent = diagram.getLeaf(code);
                if (ent == null) {
                    throw new IllegalArgumentException("No entity of the code:" + code);
                }
        	}
        } else {
            ent = diagram.getOrCreateLeaf(diagram.buildLeafIdent(idShort), code, type, null);
        }
		ent.setDisplay(Display.getWithNewlines(display));

		if (stereotype != null) {
			ent.setStereotype(new Stereotype(stereotype, diagram.getSkinParam().getCircledCharacterRadius(),
                                             diagram.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER),
                                             diagram.getSkinParam().getIHtmlColorSet()));

		}
		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			ent.addUrl(url);
		}

		Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());

		final HColor lineColor = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("LINECOLOR", 1));
		if (lineColor != null) {
			colors = colors.add(ColorType.LINE, lineColor);
		}
		if (arg.get("LINECOLOR", 0) != null) {
			colors = colors.addLegacyStroke(arg.get("LINECOLOR", 0));
		}
		ent.setColors(colors);

		final String addFields = arg.get("ADDFIELD", 0);
		if (addFields != null) {
			ent.getBodier().addFieldOrMethod(addFields);
		}
		return CommandExecutionResult.ok();
    }


}

