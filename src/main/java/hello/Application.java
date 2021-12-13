package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@SpringBootApplication
@RestController
public class Application {
    private Random random = new Random();

  static class Self {
    public String href;
  }

  static class Links {
    public Self self;
  }

  static class PlayerState {
    public Integer x;
    public Integer y;
    public String direction;
    public Boolean wasHit;
    public Integer score;
  }

  static class Arena {
    public List<Integer> dims;
    public Map<String, PlayerState> state;
  }

  static class ArenaUpdate {
    public Links _links;
    public Arena arena;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.initDirectFieldAccess();
  }

  @GetMapping("/")
  public String index() {
    return "Let the battle begin!";
  }

  @PostMapping("/**")
  public String index(@RequestBody ArenaUpdate arenaUpdate) {
    System.out.println(arenaUpdate);
    PlayerState myState = arenaUpdate.arena.state.get(arenaUpdate._links.self.href);
    Collection<PlayerState> players = arenaUpdate.arena.state.values();

    if (myState != null && isEnemyInFront(players, myState)) {
        return "T";
    } else {
        return new String[]{"F", "L", "R"}[random.nextInt(3)];
    }
  }

  private boolean isEnemyInFront(Collection<PlayerState> playersStates, PlayerState myState) {
        int myX = myState.x;
        int myY = myState.y;
        switch (myState.direction.toUpperCase()) {
            case "N": myX--; break;
            case "S": myX++; break;
            case "W": myY--; break;
            case "E": myY++; break;
        }

        final int myFrontX = myX;
        final int myFrontY = myY;
        return playersStates.stream().anyMatch(player -> player.x == myFrontX && player.y == myFrontY);
  }

}

