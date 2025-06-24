import express from 'express';
import morgan from 'morgan';
import taskRouter from './routes/router';

const app = express();

app.use(morgan('dev'));
app.use(express.json());

app.use("/api/tasks", taskRouter);

app.listen(3000, () => {
    console.log('Server is running on port 3000');
})