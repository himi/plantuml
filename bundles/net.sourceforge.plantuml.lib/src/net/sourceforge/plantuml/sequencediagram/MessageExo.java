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
package net.sourceforge.plantuml.sequencediagram;

import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.style.StyleBuilder;

public class MessageExo extends AbstractMessage {

	final private MessageExoType type;
	final private Participant participant;
	final private boolean shortArrow;

	public MessageExo(StyleBuilder styleBuilder, Participant p, MessageExoType type, Display label,
			ArrowConfiguration arrowConfiguration, String messageNumber, boolean shortArrow) {
		super(styleBuilder, label, arrowConfiguration, messageNumber);
		this.participant = p;
		this.type = type;
		this.shortArrow = shortArrow;
	}

	public boolean isShortArrow() {
		return shortArrow;
	}

	@Override
	protected NotePosition overideNotePosition(NotePosition notePosition) {
		if (type == MessageExoType.FROM_LEFT || type == MessageExoType.TO_LEFT) {
			return NotePosition.RIGHT;
		}
		if (type == MessageExoType.FROM_RIGHT || type == MessageExoType.TO_RIGHT) {
			return NotePosition.LEFT;
		}
		throw new IllegalStateException();
	}

	@Override
	public Participant getParticipant1() {
		return participant;
	}

	@Override
	public Participant getParticipant2() {
		return participant;
	}

	public Participant getParticipant() {
		return participant;
	}

	public final MessageExoType getType() {
		return type;
	}

	public boolean dealWith(Participant someone) {
		return participant == someone;
	}

	@Override
	public boolean compatibleForCreate(Participant p) {
		return p == participant;
	}

	public boolean isSelfMessage() {
		return false;
	}

}
