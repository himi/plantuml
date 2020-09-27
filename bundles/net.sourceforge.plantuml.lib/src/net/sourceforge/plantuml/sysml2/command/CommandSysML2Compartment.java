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

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Bodier;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Stereotag;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.sysml2.SysML2Diagram;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class CommandSysML2Compartment extends CommandMultilines2<SysML2Diagram> {
	public CommandSysML2Compartment() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE);
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandSysML2Compartment.class.getName(),
                                 RegexLeaf.start(), //
                                 new RegexLeaf("comp"), //
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
                                 new RegexLeaf("STEREO", "(\\<\\<.*\\>\\>)?"), //
                                 RegexLeaf.spaceZeroOrMore(), //
                                 new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
                                 RegexLeaf.spaceZeroOrMore(), //
                                 color().getRegex(), //
                                 RegexLeaf.spaceZeroOrMore(), //
                                 new RegexOptional(new RegexLeaf("LINECOLOR", "##(?:\\[(dotted|dashed|bold)\\])?(\\w+)?")), //
                                 RegexLeaf.spaceZeroOrMore(), //
                                 new RegexLeaf("\\{"), //
                                 RegexLeaf.spaceZeroOrMore(), //
                                 RegexLeaf.end());
	}

	@Override
	public boolean syntaxWithFinalBracket() {
		return true;
	}

	@Override
	public String getPatternEnd() {
		return "(?i)^[%s]*\\}[%s]*$";
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeNow(SysML2Diagram diagram, BlocLines lines) {
		lines = lines.trimSmart(1);
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
		final IEntity entity = executeArg0(diagram, line0);
		if (entity == null) {
			return CommandExecutionResult.error("No such entity");
		}
        Bodier bodier = entity.getBodier();
        bodier.setEnhanced();

		if (lines.size() > 1) {
			lines = lines.subExtract(1, 1);
			for (StringLocated s : lines) {
				if (s.getString().length() > 0 && VisibilityModifier.isVisibilityCharacter(s.getString())) {
					diagram.setVisibilityModifierPresent(true);
				}
				bodier.addFieldOrMethod(s.getString());
			}
		}

		addTags(entity, line0.get("TAGS", 0));

		return CommandExecutionResult.ok();
	}

	public static void addTags(IEntity entity, String tags) {
		if (tags == null) {
			return;
		}
		for (String tag : tags.split("[ ]+")) {
			assert tag.startsWith("$");
			tag = tag.substring(1);
			entity.addStereotag(new Stereotag(tag));
		}
	}

	private IEntity executeArg0(SysML2Diagram diagram, RegexResult arg) {
        //
        final LeafType type;
        if ("usage".equals(arg.get("UD", 0))) {
            type = LeafType.USAGE;
        } else {
            type = LeafType.CLASS;
        }

		final String idShort = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.getLazzy("CODE", 0),
				"\"([:");
		final Ident ident = diagram.buildLeafIdent(idShort);
		final Code code = diagram.buildCode(idShort);
		final String display = arg.getLazzy("DISPLAY", 0);
		final String generic = arg.get("GENERIC", 0);

		final String stereotype = arg.get("STEREO", 0);

		ILeaf result;
        if (diagram.leafExist(code)) {
            result = diagram.getOrCreateLeafForCompartment(ident, code, null, null);
            if (result.muteToType(type, null) == false) {
                return null;
            }
        } else {
            result = diagram.createLeaf(ident, code, Display.getWithNewlines(display), type, null);
        }
		if (stereotype != null) {
			result.setStereotype(new Stereotype(stereotype, diagram.getSkinParam().getCircledCharacterRadius(),
                                                diagram.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER),
                                                diagram.getSkinParam().getIHtmlColorSet()));
		}

		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			result.addUrl(url);
		}

		Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());

		final HColor lineColor = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("LINECOLOR", 1));
		if (lineColor != null) {
			colors = colors.add(ColorType.LINE, lineColor);
		}
		if (arg.get("LINECOLOR", 0) != null) {
			colors = colors.addLegacyStroke(arg.get("LINECOLOR", 0));
		}
		result.setColors(colors);

		if (generic != null) {
			result.setGeneric(generic);
		}
		return result;
	}
}

