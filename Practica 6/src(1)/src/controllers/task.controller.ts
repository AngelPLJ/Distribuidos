import {db} from '../db/config';
import { Request, Response } from 'express';
import { Task } from '../models/task';

export class TaskController {
    static async getAllTasks(req: Request, res: Response) {
        try {
            const database = await db;
            const tasks = database.data?.tasks || [];
            
            if(!tasks) {
                res.status(404).json({ message: 'No se encontraron tareas' });
                return 
            }
            if (tasks.length === 0) {
                res.status(404).json({ message: 'No se encontraron tareas' });
                return
            }
            res.status(200).json(tasks);
            return
        } catch (error) {
            console.error(error);
            res.status(500).json({ message: 'Error al obtener tareas' });
            return
        }
    }
    static async getTaskById(req: Request, res: Response) {
        try {
            const id = req.params.id;
            const database = await db;
            const tasks = database.data?.tasks || [];

            if (!tasks || tasks.length === 0) {
                res.status(404).json({ message: 'Error al obtener tareas' });
                return 
            }
            const task = tasks.find((task) => task.id === id);
            if (!task) {
                res.status(404).json({ message: 'Tarea no encontrada' });
                return 
            }
            
            res.status(200).json(task);
            return
        } catch (error) {

            console.error(error);
            res.status(500).json({ message: 'Error retrieving tasks' });
            return
        }
    }
    static async createTask(req: Request, res: Response) {
        try {
            const database = await db;
            const tasks = database.data?.tasks || [];
            const newTask:Task = req.body;

            if (!newTask.name || !newTask.description) {
                res.status(400).json({ message: 'Nombre y descripcion requerida'});
                return 
            }
            if (tasks.length === 0) {
                newTask.id = '1';
            } else {
                newTask.id = (parseInt(tasks[tasks.length - 1].id) + 1).toString();
            }

            newTask.createdAt = new Date();
            newTask.updatedAt = new Date();
            newTask.status = 'pending';

            tasks.push(newTask);
            database.data.tasks = tasks;
            await database.write();
            res.status(201).json(newTask);
            return
        } catch (error) {
            console.error(error);
            res.status(500).json({ message: 'Error al crear task' });
            return
        }
    }
    static async updateTask(req: Request, res: Response) {
        try {
            const id = req.params.id;
            const database = await db;
            const tasks = database.data?.tasks || [];
            const updatedTask = req.body;

            if (!updatedTask.name || !updatedTask.description) {
                 res.status(400).json({ message: 'Name and description are required' });
                 return
            }
            const taskIndex = tasks.findIndex((task) => task.id === id);
            if (taskIndex === -1) {
                res.status(404).json({ message: 'Task not found' });
                return 
            }
            tasks[taskIndex] = { ...tasks[taskIndex], ...updatedTask };
            database.data.tasks = tasks;
            await database.write();
            res.status(200).json(tasks[taskIndex]);
            return
        } catch (error) {
            console.error(error);
            res.status(500).json({ message: 'Error updating task' });
            return
        }
    }
    static async deleteTask(req: Request, res: Response) {  
        try {
            const id = req.params.id;
            const database = await db;
            const tasks = database.data?.tasks || [];

            if (tasks.length === 0) {
                res.status(404).json({ message: 'Error al obtener tareas' });
                return 
            }
            const taskIndex = tasks.findIndex((task) => task.id === id);
            if (taskIndex === -1) {
               res.status(404).json({ message: 'Tarea no encontrada' });
               return 
            }
            tasks.splice(taskIndex, 1);
            database.data.tasks = tasks;
            await database.write();
            res.status(200).json({ message: 'Tarea eliminada' });
            return
        } catch (error) {
            console.error(error);
            res.status(500).json({ message: 'Error al eliminar tarea' });
            return
        }
    }
} 