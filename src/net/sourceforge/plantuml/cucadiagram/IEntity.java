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
package net.sourceforge.plantuml.cucadiagram;

import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.Hideable;
import net.sourceforge.plantuml.LineConfigurable;
import net.sourceforge.plantuml.Removeable;
import net.sourceforge.plantuml.SpecificBackcolorable;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.USymbol;

public interface IEntity extends SpecificBackcolorable, Hideable, Removeable, LineConfigurable {

	public Code getCode();

	public String getCodeGetName();

	public Ident getIdent();

	public USymbol getUSymbol();

	public void setUSymbol(USymbol symbol);

	public LeafType getLeafType();

	public Display getDisplay();

	public IGroup getParentContainer();

	public void setDisplay(Display display);

	public String getUid();

	public Url getUrl99();

	public Stereotype getStereotype();

	public void setStereotype(Stereotype stereotype);

	public Bodier getBodier();

	public void addUrl(Url url);

	public boolean isGroup();

	public boolean hasUrl();

	public int getHectorLayer();

	public void setHectorLayer(int layer);

	public int getRawLayout();

	public void putTip(String member, Display display);

	public Map<String, Display> getTips();

	public void addStereotag(Stereotag tag);

	public Set<Stereotag> stereotags();
	
	public boolean isAloneAndUnlinked();


}
