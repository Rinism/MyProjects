import { Game } from "./Game";

describe("Tic-Tac-Toe", () => {
  it("should start with blank state", () => {
    const game = new Game();

    expect(game.getCells()).toEqual([
      "-", "-", "-",
      "-", "-", "-",
      "-", "-", "-"
    ]);
    expect(game.getTurn()).toBe("X");
    expect(game.getWinner()).toBe("-");
    expect(game.isTie()).toBe(false);
  });

  it("should be able to make a move", () => {
    const game = new Game();
    game.onClick(0);
    game.onClick(1);
    game.onClick(2);

    expect(game.getCells()).toEqual([
      "X",
       "O",
        "X",
      "-",
       "-",
        "-",
      "-",
       "-",
        "-"
    ]);
    expect(game.getTurn()).toBe("O");
    expect(game.getWinner()).toBe("-");
    expect(game.isTie()).toBe(false);
  });

  
  it("should be able to make a move only once in same cell", () => {
    const game = new Game();
    game.onClick(0);
    game.onClick(0);

    expect(game.getCells()).toEqual([
      "X",
       "-",
        "-",
      "-",
       "-",
        "-",
      "-",
       "-",
        "-"
    ]);
    expect(game.getTurn()).toBe("O");
    expect(game.getWinner()).toBe("-");
    expect(game.isTie()).toBe(false);
  });

  it("should be able to win on first horizontal line", () => {
    const game = new Game();
    game.onClick(0);
    game.onClick(3);
    game.onClick(1);
    game.onClick(4);
    game.onClick(2);

    expect(game.getCells()).toEqual([
      "X",
       "X",
        "X",
      "O",
       "O",
        "-",
      "-",
       "-",
        "-"
    ]);
    expect(game.getWinner()).toBe("X");
    expect(game.isTie()).toBe(false);
  });

  it("should be able to win on first vertical line", () => {
    const game = new Game();
    game.onClick(0);
    game.onClick(1);
    game.onClick(3);
    game.onClick(4);
    game.onClick(6);

    expect(game.getCells()).toEqual([
      "X",
       "O",
        "-",
      "X",
       "O",
        "-",
      "X",
       "-",
        "-"
    ]);
    expect(game.getWinner()).toBe("X");
    expect(game.isTie()).toBe(false);
  });
  it("should be able to win on first diagonal line", () => {
    const game = new Game();
    game.onClick(0);
    game.onClick(1);
    game.onClick(4);
    game.onClick(2);
    game.onClick(8);

    expect(game.getCells()).toEqual([
      "X",
       "O",
        "O",
      "-",
       "X",
        "-",
      "-",
       "-",
        "X"
    ]);
    expect(game.getWinner()).toBe("X");
    expect(game.isTie()).toBe(false);
  });

  it("should be able to get tie", () => {
    const game = new Game();
    game.onClick(0);
    game.onClick(1);
    game.onClick(3);
    game.onClick(6);
    game.onClick(4);
    game.onClick(8);
    game.onClick(2);
    game.onClick(5);
    game.onClick(7);

    expect(game.getCells()).toEqual([
      "X",
       "O",
        "X",
      "X",
       "X",
        "O",
      "O",
       "X",
        "O"
    ]);
    expect(game.getWinner()).toBe("-");
    expect(game.isTie()).toBe(true);
  });
});