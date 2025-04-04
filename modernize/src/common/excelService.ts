import * as XLSX from 'xlsx';

export type ParseMap<T> = { [K in keyof T]: string };

export async function parseExcel<T extends Record<string, any>>(file: File): Promise<T[]> {
    const data = await file.arrayBuffer();
    const workbook = XLSX.read(data, { type: 'array' });
    const sheetName = workbook.SheetNames[0];
    const worksheet = workbook.Sheets[sheetName];

    const jsonData = XLSX.utils.sheet_to_json<Record<string, any>>(worksheet, { header: 1 });
    const [headers, ...body] = jsonData;

    return body.map(row => {
        const item: Partial<T> = {};
        (headers as string[]).forEach((header, index) => {
            item[header as keyof T] = row[index];
        });
        return item as T;
    });
}