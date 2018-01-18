import com.nothardcoded.sceneserver.scene.model.annotation.AsyncSceneObjectProperty;
import com.nothardcoded.sceneserver.scene.model.annotation.SceneAutowire;
import com.nothardcoded.sceneserver.scene.model.annotation.ScenePropertyQualifier;
import com.nothardcoded.sceneserver.scene.model.object.SceneObject;
import com.nothardcoded.sceneserver.scene.model.property.SceneObjectProperty;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;

class DynamicObject extends SceneObject {
  private SceneObjectProperty<String> someOtherVal = new SceneObjectProperty<>();

  @ScenePropertyQualifier("sampleInt2")
  @AsyncSceneObjectProperty(updateTime = 1000)
  private SceneObjectProperty<Integer> sampleValue;

  public DynamicObject (@ScenePropertyQualifier("someOtherValue2") String someOtherVal) {
    this.someOtherVal.setValue(someOtherVal);
  }

  public SceneObjectProperty<String> getSomeOtherVal() {
    return someOtherVal;
  }

  public void setSomeOtherVal(SceneObjectProperty<String> someOtherVal) {
    this.someOtherVal = someOtherVal;
  }

  public SceneObjectProperty<Integer> getSampleValue() {
    return sampleValue;
  }

  public void setSampleValue(SceneObjectProperty<Integer> sampleValue) {
    this.sampleValue = sampleValue;
  }
}