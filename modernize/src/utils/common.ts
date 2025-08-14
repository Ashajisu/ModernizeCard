import apiClient from '@/data/Axios';

function formatMoney(value: any) {
    if (value === null || value === undefined || value === '') return '';
    const number = parseInt(value.toString().replace(/[^0-9\-]/g, ''));
    const result = isNaN(number) ? '' : number.toLocaleString(); // 12,345 형식
    return result;
}

const saveToServer = async (validateForm: any, url: string) => {
    const formData = await validateForm();
    try {
        const response = await apiClient.post(url, formData);
        console.log('서버 저장:', response);
        return response;
    } catch (error) {
        console.error('서버 저장 실패:', error);
        throw error;
    }
};

const deleteToServer = async (id: string, url: string) => {
    try {
        const response = await apiClient.post(url, { id : id});
        if (response.status === 200) {
            console.log('서버 삭제:', response);
            return id;
        }
    } catch (error) {
        console.error('서버 저장 실패:', error);
        throw error;
    }
};

const excelSaveToServer = async (data: any, url: string) => {
    const list = Array.isArray(data) ? data : [data];
    try {
        const response = await apiClient.post(url, list);
        console.log('excel 서버 저장:');
        return response;
    } catch (error) {
        console.error('excel 서버 저장 실패:', error);
        throw error;
    }
};
export { formatMoney, saveToServer, deleteToServer, excelSaveToServer };