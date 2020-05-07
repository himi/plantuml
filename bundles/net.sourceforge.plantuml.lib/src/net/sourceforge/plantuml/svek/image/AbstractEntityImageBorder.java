/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
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
 * Original Author:  Hisashi Miyashita
 */

package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Rankdir;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.Cluster;
import net.sourceforge.plantuml.svek.ShapeType;

public abstract class AbstractEntityImageBorder extends AbstractEntityImage {
    public final EntityPosition entityPosition;
	protected final Cluster parent;
	protected final Bibliotekon bibliotekon;
	protected final Rankdir rankdir;

	protected final TextBlock desc;

    AbstractEntityImageBorder(ILeaf leaf, ISkinParam skinParam, Cluster parent,
    	                      Bibliotekon bibliotekon,
                             FontParam fontParam) {
    	super(leaf, skinParam);

        this.parent = parent;
		this.bibliotekon = bibliotekon;
		this.entityPosition = leaf.getEntityPosition();
		this.rankdir = skinParam.getRankdir();

		if (entityPosition == EntityPosition.NORMAL) {
			throw new IllegalArgumentException();
		}

		final Stereotype stereotype = leaf.getStereotype();
        final FontConfiguration fc = new FontConfiguration(skinParam, fontParam, stereotype);
		this.desc = leaf.getDisplay().create(fc, HorizontalAlignment.CENTER, skinParam);
    }

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return entityPosition.getDimension(rankdir);
	}

	public double getMaxWidthFromLabelForEntryExit(StringBounder stringBounder) {
		final Dimension2D dimDesc = desc.calculateDimension(stringBounder);
		return dimDesc.getWidth();
	}

	public ShapeType getShapeType() {
		return entityPosition.getShapeType();
	}

}