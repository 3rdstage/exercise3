package thirdstage.sirius.support.web3j;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.quorum.Quorum;

/**
 * @author 3rdstage
 * @since 2018-04-09
 */
@Component
public abstract class AbstractQuorumContract{

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  private Quorum defaultQuorum;

  protected Quorum getDefaultQuorum() { return this.defaultQuorum; }

  private Quorum fallbackQuorum;

  protected Quorum getFallbackQuorum() { return this.fallbackQuorum; }

  private Quorum quorum;

  protected Quorum getQuorum() { return this.quorum; }

  @Value("${quorum.from}")
  private String from;

  public String getFrom() { return this.from; }

  protected AbstractQuorumContract setFrom(@NotEmpty final String from) {
    this.from = from;
    return this;
  }

  abstract protected String getAddress();

  public AbstractQuorumContract(@Nonnull final Quorum defaultQr, @Nonnull final Quorum fallbackQr) {
    this.defaultQuorum = defaultQr;
    this.fallbackQuorum = fallbackQr;
  }

  @PostConstruct
  protected void postConstruct() {
    this.quorum = this.defaultQuorum;
  }
}
