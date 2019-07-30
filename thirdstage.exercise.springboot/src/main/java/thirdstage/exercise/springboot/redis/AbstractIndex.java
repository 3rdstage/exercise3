package thirdstage.exercise.springboot.redis;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractIndex<K> implements Index<K>{


  private String dataName;

  private String name;

  @Override
  public String getDataName() {
    return this.dataName;

  }

  @Override
  public String getName() {
    return this.name;
  }

  public AbstractIndex(@NotBlank String dataName, @NotBlank String name) {

    Validate.isTrue(StringUtils.isNotBlank(dataName), "Target data name should be specified.");
    Validate.isTrue(StringUtils.isNotBlank(name), "Name for this index should be specified.");

    this.dataName = dataName;
    this.name = name;

  }

}
