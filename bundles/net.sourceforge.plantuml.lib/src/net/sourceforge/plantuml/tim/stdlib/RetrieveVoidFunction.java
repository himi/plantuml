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
package net.sourceforge.plantuml.tim.stdlib;

import java.util.List;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TFunction;
import net.sourceforge.plantuml.tim.TFunctionSignature;
import net.sourceforge.plantuml.tim.TMemory;
import net.sourceforge.plantuml.tim.expression.TValue;

public class RetrieveVoidFunction extends SimpleReturnFunction {

	public TFunctionSignature getSignature() {
		return new TFunctionSignature("%retrieve_void_func", 1);
	}

	public boolean canCover(int nbArg) {
		return nbArg > 0;
	}

	public TValue executeReturn(TContext context, TMemory memory, LineLocation location, List<TValue> values) throws EaterException, EaterExceptionLocated {
		final String fname = values.get(0).toString();
		final List<TValue> args = values.subList(1, values.size());
		final TFunctionSignature signature = new TFunctionSignature(fname, args.size());
		final TFunction func = context.getFunctionSmart(signature);
		final int n1 = context.getResultList().size();
		func.executeVoidInternal(context, memory, args);
		final String extracted = context.extractFromResultList(n1);
		return TValue.fromString(extracted);
	}

}