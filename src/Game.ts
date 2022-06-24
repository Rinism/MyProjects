export type XO = "X" | "O" | "-";

export class Game {

  cells: XO[] = ["-", "-", "-", "-", "-", "-", "-", "-", "-"];
  gameistie: boolean = false;

  getCells(): XO[] {
    return this.cells;
  }

  getTurn(): XO {
    const countEmptyCells = this.cells.filter(cell => cell == "-").length
    return countEmptyCells % 2 === 0 ? "O" : "X";
  }

  getWinner(): XO {

    //check horizontal 
    if (this.cells[0] === this.cells[1] && this.cells[1] === this.cells[2] && this.cells[1] !== "-") {
      return this.cells[0];
    } else if (this.cells[3] === this.cells[4] && this.cells[4] === this.cells[5] && this.cells[4] !== "-") {
      return this.cells[3];
    } else if (this.cells[6] === this.cells[7] && this.cells[7] === this.cells[8] && this.cells[7] !== "-") {
      return this.cells[6];
    }
    //check vertical
    else if (this.cells[0] === this.cells[3] && this.cells[3] === this.cells[6] && this.cells[3] !== "-") {
      return this.cells[0];
    } else if (this.cells[1] === this.cells[4] && this.cells[4] === this.cells[7] && this.cells[4] !== "-") {
      return this.cells[1];
    } else if (this.cells[2] === this.cells[5] && this.cells[5] === this.cells[8] && this.cells[5] !== "-") {
      return this.cells[2];
    }

    //check diagonal
    else if (this.cells[0] === this.cells[4] && this.cells[4] === this.cells[8] && this.cells[4] !== "-") {
      return this.cells[0];
    } else if (this.cells[6] === this.cells[4] && this.cells[4] === this.cells[2] && this.cells[4] !== "-") {
      return this.cells[6];
    }
    return "-";
  }

  isTie(): boolean {
    if (this.getWinner() === "-" && !this.cells.includes("-")) {
      return true;
    }
    return false;
  }

  onClick(i: number): void {
    if (this.cells[i] === "-") {
      this.cells[i] = this.getTurn();
    }
    if (this.getWinner() !== "-" || this.isTie() == true) {
      Object.freeze(this.cells);
    }
  }

  restart(): void {
    if (this.getWinner() !== "-" || this.isTie() == true) {
      location.reload(true);
    }
    console.log("restart called");
  }
}