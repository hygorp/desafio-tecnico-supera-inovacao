import React, {useEffect} from "react";
import {AppContext} from "@/context/app-context";

export function AppContextProvider({ children } : { children : React.ReactNode }) {
    const API_URL: string  = process.env.NEXT_PUBLIC_API_URL ?? ":";

    const [tasks, setTasks] = React.useState<Set<Task>>(new Set());

    useEffect(() => {
        const loadResources = async () => {
            await fetch(API_URL.concat("/tasks/find-all"), {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            }).then(async (response: Response) => {
                if (response.ok) {
                    const data = await response.json();

                    setTasks(data);
                }
            })
        }

        loadResources().then();
    }, [API_URL]);

    const saveTask = async (task: Task): Promise<number> => {
        const response = await fetch(API_URL.concat("/tasks/save"), {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                title: task.title,
                description: task.description,
                expiresAt: task.expiresAt
            })
        })

        if (response.status === 201) {
            const data = await response.json();

            updateState(data);
        }

        return response.status;
    }

    const updateTask = async (taskId: string, task: Task): Promise<number> => {
        const response = await fetch(API_URL.concat(`/tasks/update/${taskId}`), {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                id: task.id,
                title: task.title,
                description: task.description,
                expiresAt: task.expiresAt,
            })
        });

        if (response.status === 200) {
            const data = await response.json();

            updateState(data);
        }

        return response.status;
    }

    const deleteTask = async (taskId: string): Promise<number> => {
        const response = await fetch(API_URL.concat(`/tasks/delete/${taskId}`), {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.status === 204) {
            setTasks((prevTasks) => {
                const updatedTasks = new Set(prevTasks);

                updatedTasks.forEach((task) => {
                    if (task.id === taskId) {
                        updatedTasks.delete(task);
                    }
                });

                return updatedTasks;
            });
        }

        return response.status;
    }

    const addItem = async (taskId: string, item: Item): Promise<number> => {
        const response = await fetch(API_URL.concat(`/tasks/task/${taskId}/add-item`), {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                title: item.title,
                description: item.description,
                priority: item.priority,
                state: item.state
            })
        })

        if (response.status === 200) {
            const data = await response.json();

            updateState(data);
        }

        return response.status;
    }

    const updateItem = async (taskId: string, item: Item): Promise<number> => {
        const response = await fetch(API_URL.concat(`/tasks/task/${taskId}/update-item`), {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                id: item.id,
                title: item.title,
                description: item.description,
                priority: item.priority,
                state: item.state
            })
        });

        if (response.status === 200) {
            const data = await response.json();

            updateState(data);
        }

        return response.status;
    }

    const deleteItem = async (taskId: string, item: Item): Promise<number> => {
        const response = await fetch(API_URL.concat(`/tasks/task/${taskId}/delete-item`), {
            method: "DELETE",
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify(item)
        });

        if (response.status === 200) {
            const data = await response.json();

            updateState(data);
        }

        return response.status;
    }

    const clearItems = async (taskId: string): Promise<number> => {
        const response = await fetch(API_URL.concat(`/tasks/task/${taskId}/clear-items`), {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.status === 200) {
            const data = await response.json();

            updateState(data);
        }

        return response.status;
    }

    function updateState(data: Task) {
        setTasks((prevTasks) => {
            const updatedTasks = new Set(prevTasks);

            updatedTasks.forEach((existingTask) => {
                if (existingTask.id === data.id) {
                    updatedTasks.delete(existingTask);
                }
            });

            updatedTasks.add(data);

            return updatedTasks;
        });
    }

    return (
        <AppContext.Provider value={{tasks, saveTask, updateTask, deleteTask, addItem, updateItem, deleteItem, clearItems}}>
            {children}
        </AppContext.Provider>
    )
}