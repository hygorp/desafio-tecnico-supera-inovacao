import {createContext} from "react";

interface AppContext {
    tasks: Set<Task>
    saveTask: (task: Task) => Promise<number>
    updateTask: (taskId: string, task: Task) => Promise<number>
    deleteTask: (taskId: string) => Promise<number>
    addItem: (taskId: string, item: Item) => Promise<number>
    updateItem: (taskId: string, item: Item) => Promise<number>
    deleteItem: (taskId: string, item: Item) => Promise<number>
    clearItems: (taskId: string) => Promise<number>
}

export const AppContext = createContext({} as AppContext)