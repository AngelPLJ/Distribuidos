export type Task = {
    id: string;
    name: string;
    description: string;
    status: TaskStatus;
    createdAt: Date;
    updatedAt: Date;

    //opcionales
    dueDate?: Date; 
    priority?: TaskPriority; 
    tags?: string[]; 
}
export type TaskStatus = 'pending' | 'in-progress' | 'completed';
export type TaskPriority = 'low' | 'medium' | 'high';
export type databaseSchema = {
    tasks:Task[]
}