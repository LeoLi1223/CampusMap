export interface ShortestPath {
    cost: number,
    path: Segment[],
    start: {
        x: number,
        y: number
    }
}

export interface Segment {
    start: {
        x: number,
        y: number
    }
    end: {
        x: number,
        y: number
    }
    cost: number
}

export interface Building {
    shortName: string,
    longName: string
    x: number,
    y: number
}