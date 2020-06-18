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
package net.sourceforge.plantuml.descdiagram.command;

import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;

public class CommandCreateElementMultilines extends CommandMultilines2<AbstractEntityDiagram> {

	private final int type;

	enum Mode {
		EXTENDS, IMPLEMENTS
	};

	public CommandCreateElementMultilines(int type) {
		super(getRegexConcat(type), MultilinesStrategy.REMOVE_STARTING_QUOTE);
		this.type = type;
	}

	@Override
	public String getPatternEnd() {
		if (type == 0) {
			return "(?i)^(.*)[%g]$";
		}
		if (type == 1) {
			return "(?i)^(.*)\\]$";
		}
		throw new IllegalArgumentException();
	}

	private static RegexConcat getRegexConcat(int type) {
		if (type == 0) {
			return RegexConcat.build(CommandCreateElementMultilines.class.getName() + type, RegexLeaf.start(), //
					new RegexLeaf("TYPE", "(" + CommandCreateElementFull.ALL_TYPES + ")[%s]+"), //
					new RegexLeaf("CODE", "([\\p{L}0-9_.]+)"), //
					RegexLeaf.spaceZeroOrMore(), //
					new RegexLeaf("STEREO", "(\\<\\<.+\\>\\>)?"), //
					RegexLeaf.spaceZeroOrMore(), //
					new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
					RegexLeaf.spaceZeroOrMore(), //
					ColorParser.exp1(), //
					RegexLeaf.spaceZeroOrMore(), //
					new RegexLeaf("as"), //
					RegexLeaf.spaceZeroOrMore(), //
					new RegexLeaf("[%g]"), //
					new RegexLeaf("DESC", "([^%g]*)"), //
					RegexLeaf.end());
		}
		if (type == 1) {
			return RegexConcat.build(CommandCreateElementMultilines.class.getName() + type, RegexLeaf.start(), //
					new RegexLeaf("TYPE", "(" + CommandCreateElementFull.ALL_TYPES + ")[%s]+"), //
					new RegexLeaf("CODE", "([\\p{L}0-9_.]+)"), //
					RegexLeaf.spaceZeroOrMore(), //
					new RegexLeaf("STEREO", "(\\<\\<.+\\>\\>)?"), //
					RegexLeaf.spaceZeroOrMore(), //
					new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
					RegexLeaf.spaceZeroOrMore(), //
					ColorParser.exp1(), //
					RegexLeaf.spaceZeroOrMore(), //
					new RegexLeaf("\\["), //
					new RegexLeaf("DESC", "(.*)"), //
					RegexLeaf.end());
		}
		throw new IllegalArgumentException();
	}

	@Override
	protected CommandExecutionResult executeNow(AbstractEntityDiagram diagram, BlocLines lines) {
		lines = lines.trim(false);
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst499().getTrimmed().getString());
		final String symbol = StringUtils.goUpperCase(line0.get("TYPE", 0));
		final LeafType type;
		USymbol usymbol;

		if (symbol.equalsIgnoreCase("usecase")) {
			type = LeafType.USECASE;
			usymbol = null;
		} else {
			usymbol = USymbol.getFromString(symbol, diagram.getSkinParam().getActorStyle());
			if (usymbol == null) {
				throw new IllegalStateException();
			}
			type = LeafType.DESCRIPTION;
		}

		final String idShort = line0.get("CODE", 0);
		final List<String> lineLast = StringUtils.getSplit(MyPattern.cmpile(getPatternEnd()), lines.getLast499()
				.getString());
		lines = lines.subExtract(1, 1);
		Display display = lines.toDisplay();
		final String descStart = line0.get("DESC", 0);
		if (StringUtils.isNotEmpty(descStart)) {
			display = display.addFirst(descStart);
		}

		if (StringUtils.isNotEmpty(lineLast.get(0))) {
			display = display.add(lineLast.get(0));
		}

		final String stereotype = line0.get("STEREO", 0);

		final Ident ident = diagram.buildLeafIdent(idShort);
		final Code code = diagram.V1972() ? ident : diagram.buildCode(idShort);
		if (CommandCreateElementFull.existsWithBadType3(diagram, code, ident, type, usymbol)) {
			return CommandExecutionResult.error("This element (" + code.getName() + ") is already defined");
		}
		final ILeaf result = diagram.createLeaf(ident, code, display, type, usymbol);
		if (result == null) {
			return CommandExecutionResult.error("This element (" + code.getName() + ") is already defined");
		}
		result.setUSymbol(usymbol);
		if (stereotype != null) {
			result.setStereotype(new Stereotype(stereotype, diagram.getSkinParam().getCircledCharacterRadius(), diagram
					.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER), diagram.getSkinParam()
					.getIHtmlColorSet()));
		}

		final String urlString = line0.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			result.addUrl(url);
		}

		result.setSpecificColorTOBEREMOVED(ColorType.BACK,
				diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(line0.get("COLOR", 0)));

		return CommandExecutionResult.ok();
	}
}
