import { JSONFilePreset } from "lowdb/node";
import { databaseSchema, Task } from "../models/task";

const defaultData:databaseSchema = {
    tasks:[]
}
async function initializeDatabase() {
    const db = await JSONFilePreset<databaseSchema>('./db.json', defaultData);
    return db;
}

export const db = initializeDatabase();
