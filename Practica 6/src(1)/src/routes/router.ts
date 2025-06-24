import {Router} from 'express';
import { TaskController } from '../controllers/task.controller';

const taskRouter = Router();    

taskRouter.get('/get-all', TaskController.getAllTasks);
taskRouter.get('/get-by-id/:id', TaskController.getTaskById);
taskRouter.post('/create', TaskController.createTask);
taskRouter.delete('/delete/:id', TaskController.deleteTask);
taskRouter.put('/update/:id', TaskController.updateTask);

export default taskRouter;