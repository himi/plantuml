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
package net.sourceforge.plantuml.tim;

public class TFunctionSignature {

	private final String functionName;
	private final int nbArg;

	public TFunctionSignature(String functionName, int nbArg) {
		if (functionName == null) {
			throw new IllegalArgumentException();
		}
		this.functionName = functionName;
		this.nbArg = nbArg;
	}
	
	public boolean sameNameAs(TFunctionSignature other) {
		return getFunctionName().equals(other.getFunctionName());
	}

	@Override
	public String toString() {
		return functionName + "/" + nbArg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + functionName.hashCode();
		result = prime * result + nbArg;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		final TFunctionSignature other = (TFunctionSignature) obj;
		return functionName.equals(other.functionName) && nbArg == other.nbArg;
	}

	public final String getFunctionName() {
		return functionName;
	}

	public final int getNbArg() {
		return nbArg;
	}
}