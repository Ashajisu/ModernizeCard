// RecordChatType 정의
type RecordChatType = {
    id: number;
    name: string;
    status: string;
    thumb: string;
    recent: boolean;
    chatHistory: {
        createdAt: Date;
        msg: string;
        senderId: number | string;
        type: string;
        attachment: { file: string; fileSize: string }[];
        id: string;
    }[];
}