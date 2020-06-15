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
package net.sourceforge.plantuml.project.command;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.lang.Complement;
import net.sourceforge.plantuml.project.lang.ComplementEmpty;
import net.sourceforge.plantuml.project.lang.ComplementPattern;
import net.sourceforge.plantuml.project.lang.Subject;
import net.sourceforge.plantuml.project.lang.SubjectPattern;
import net.sourceforge.plantuml.project.lang.Verb;
import net.sourceforge.plantuml.project.lang.VerbPattern;

public class NaturalCommand extends SingleLineCommand2<GanttDiagram> {

	private final SubjectPattern subjectPattern;
	private final VerbPattern verbPattern;
	private final ComplementPattern complementPattern;

	private NaturalCommand(RegexConcat pattern, SubjectPattern subject, VerbPattern verb, ComplementPattern complement) {
		super(pattern);
		this.subjectPattern = subject;
		this.verbPattern = verb;
		this.complementPattern = complement;
	}

	@Override
	public String toString() {
		return subjectPattern.toString() + " " + verbPattern.toString() + " " + complementPattern.toString();
	}

	@Override
	protected CommandExecutionResult executeArg(GanttDiagram system, LineLocation location, RegexResult arg) {
		final Subject subject = subjectPattern.getSubject(system, arg);
		final Verb verb = verbPattern.getVerb(system, arg);
		final Failable<Complement> complement = complementPattern.getComplement(system, arg, "0");
		if (complement.isFail()) {
			return CommandExecutionResult.error(complement.getError());
		}
		return verb.execute(subject, complement.get());
	}

	public static Command create(SubjectPattern subject, VerbPattern verb, ComplementPattern complement) {
		final RegexConcat pattern;
		if (complement instanceof ComplementEmpty) {
			pattern = new RegexConcat(//
					RegexLeaf.start(), //
					subject.toRegex(), //
					RegexLeaf.spaceOneOrMore(), //
					verb.toRegex(), //
					RegexLeaf.end());
		} else {
			pattern = new RegexConcat(//
					RegexLeaf.start(), //
					subject.toRegex(), //
					RegexLeaf.spaceOneOrMore(), //
					verb.toRegex(), //
					RegexLeaf.spaceOneOrMore(), //
					complement.toRegex("0"), //
					RegexLeaf.end());
		}
		// System.err.println("NaturalCommand="+pattern.getPattern());
		return new NaturalCommand(pattern, subject, verb, complement);
	}
}
