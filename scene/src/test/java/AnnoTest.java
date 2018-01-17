import com.nothardcoded.sceneserver.scene.Scene;
import com.nothardcoded.sceneserver.scene.model.annotation.SceneInject;
import com.nothardcoded.sceneserver.scene.model.listener.SceneListener;
import com.nothardcoded.sceneserver.scene.model.object.SceneObject;
import com.nothardcoded.sceneserver.scene.model.property.SceneObjectProperty;
import com.nothardcoded.sceneserver.scene.model.property.SceneProperty;
import org.apache.log4j.BasicConfigurator;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

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
  public void notStaticTest () {
    BasicConfigurator.configure();

    Set<DynamicScene> test = new HashSet<>();
    for (int i = 0; i < 10000; i ++) {
      DynamicScene scene = new DynamicScene();

      System.out.println("Before: " + scene.getNotStatic().getSampleValue());
      System.out.println("Before: " + scene.getNotStatic2().getSampleValue());

      scene.getNotStatic().setSampleValue(100);
      scene.getNotStatic().getSomeOtherVal().setValue("This is a test.");

      scene.getNotStatic2().setSampleValue(200);
      scene.getNotStatic2().getSomeOtherVal().setValue("This is a test 2.");

      //test.add(scene);
    }


  }

  class DynamicScene extends Scene {
    @SceneInject
    private DynamicObject notStatic;
    @SceneInject
    private DynamicObject notStatic2;

    public DynamicScene () {
      //registerSceneObject(notStatic);
      //registerSceneObject(notStatic2);
    }

    @Override
    protected void beforeInjection () {
      registerSceneProperty(new SceneProperty("sampleInt", new Integer (10)));
      registerSceneProperty(new SceneProperty("someOtherValue", "This is before."));
      registerListener(sceneListener);
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
