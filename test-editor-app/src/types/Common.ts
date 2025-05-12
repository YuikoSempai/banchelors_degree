import { Quiz } from "./Quiz";


export interface QuizResponse {
    result: Quiz[]
}

export interface Room {
    id: number;
    name: string;
    users: string[];      // или userIds: number[]
    quizNames: string[];  // или quizIds: number[]
}

export interface UserResult {
    userName: string;
    quizResults: number[];
}