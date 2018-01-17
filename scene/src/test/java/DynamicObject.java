import com.nothardcoded.sceneserver.scene.model.annotation.AsyncSceneObjectProperty;
import com.nothardcoded.sceneserver.scene.model.object.SceneObject;
import com.nothardcoded.sceneserver.scene.model.property.SceneObjectProperty;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class DynamicObject extends SceneObject {
  private SceneObjectProperty<String> someOtherVal = new SceneObjectProperty<>();

  @AsyncSceneObjectProperty(updateTime = 1000)
  private Integer sampleValue;

  public DynamicObject (Integer integer, String someOtherVal) {
    sampleValue = integer;
    this.someOtherVal.setValue(someOtherVal);
  }

  public Integer getSampleValue() {
    return sampleValue;
  }

  public void setSampleValue(Integer sampleValue) {
    this.sampleValue = sampleValue;
  }

  public SceneObjectProperty<String> getSomeOtherVal() {
    return someOtherVal;
  }

  public void setSomeOtherVal(SceneObjectProperty<String> someOtherVal) {
    this.someOtherVal = someOtherVal;
  }
}