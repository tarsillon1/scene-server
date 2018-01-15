import com.nothardcoded.dynamicscene.scene.Scene;
import com.nothardcoded.dynamicscene.scene.model.annotation.AsyncSceneObjectProperty;
import com.nothardcoded.dynamicscene.scene.model.annotation.SceneInject;
import com.nothardcoded.dynamicscene.scene.model.listener.SceneListener;
import com.nothardcoded.dynamicscene.scene.model.object.SceneObject;
import com.nothardcoded.dynamicscene.scene.model.property.SceneObjectProperty;
import com.nothardcoded.dynamicscene.scene.model.property.SceneProperty;
import org.apache.log4j.BasicConfigurator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by nick.tarsillo on 1/11/18.
 */
public class AnnoTest {
  private SceneListener sceneListener = new SceneListener() {
    @Override
    public void newProperty(SceneObjectProperty sceneObjectProperty) {
      System.out.println("New property was created: " + sceneObjectProperty.getValue());
    }

    @Override
    public void propertyUpdated(SceneObjectProperty objectProperty) {
      System.out.println("Property was updated to: " + objectProperty.getValue());
    }

    @Override
    public void newObject(SceneObject object) {
      System.out.println("New object was created in scene: " + object);
    }

    @Override
    public void objectUpdated(SceneObject sceneObject) {
      System.out.println("Object was updated in scene: " + sceneObject);
    }
  };

  @Test
  public void notStaticTest () throws InterruptedException {
    BasicConfigurator.configure();

    DynamicScene scene = new DynamicScene();
    scene.registerListener(sceneListener);

    scene.registerSceneProperty(new SceneProperty("sampleInt", new Integer (10)));

    System.out.println("Before: " + scene.getNotStatic().getSampleValue());
    System.out.println("Before: " + scene.getNotStatic2().getSampleValue());

    scene.getNotStatic().setSampleValue(100);
    scene.getNotStatic().getSomeOtherVal().setValue("This is a test.");

    scene.getNotStatic2().setSampleValue(200);
    scene.getNotStatic2().getSomeOtherVal().setValue("This is a test 2.");

    Thread.sleep(2000);
  }

  class DynamicScene extends Scene {
    @SceneInject
    private DynamicObject notStatic;
    @SceneInject
    private DynamicObject notStatic2;

    public DynamicScene () {
      construct(notStatic);
      construct(notStatic2);
    }

    public DynamicObject getNotStatic() {
      return notStatic;
    }

    public void setNotStatic(DynamicObject notStatic) {
      this.notStatic = notStatic;
    }

    public DynamicObject getNotStatic2() {
      return notStatic2;
    }

    public void setNotStatic2(DynamicObject notStatic2) {
      this.notStatic2 = notStatic2;
    }
  }
}
