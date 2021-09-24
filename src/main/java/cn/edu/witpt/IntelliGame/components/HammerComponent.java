package cn.edu.witpt.IntelliGame.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;

/**
 * @author nIck_
 */

@Required(OwnerComponent.class)
public class HammerComponent extends Component {
    private OwnerComponent owner;
    private double speed;
    public HammerComponent(Double speed) {
        this.speed = speed;
    }

    @Override
    public void onUpdate(double tpf) {
        entity.translateY( -tpf * speed );
    }
}
