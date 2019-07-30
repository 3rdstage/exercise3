package thirdstage.exercise.springboot.redis;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexFacadeImpl implements IndexFacade{

  @Override
  public void addSimpleIndex(@NotBlank String dataName, @NotBlank String indexName, IndexType type){
    // TODO Auto-generated method stub

  }

  @Override
  public <K> void addSimpleIndexEntry(@NotBlank String dataName, @NotBlank String indexName, K indexKey, String indexValue){
    // TODO Auto-generated method stub

  }

  @Override
  public <K> String[] findSimpleIndexEntries(@NotBlank String dataName, @NotBlank String indexName, K indexKey){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String[] findSimpleDateIndexEntries(@NotBlank String dataName, @NotBlank String indexName, LocalDateTime from, LocalDateTime to){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String[] findSimpleDateIndexEntries(@NotBlank String dataName, @NotBlank String indexName, String from, String to){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String[] findSimpleDateIndexEntries(@NotBlank String dataName, @NotBlank String indexName, long from, long to){
    // TODO Auto-generated method stub
    return null;
  }


}
